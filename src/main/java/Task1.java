import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class Task1 implements Runnable {

    /**
     * Размер алфавита
     */
    private static final int ALPHABET_SIZE = 256;
    /**
     * Длины последовательностей
     */
    private static final int[] WINDOW_SIZES = {1, 2, 4, 8};
    private static final int BUFFER_SIZE = 8192;

    private final File file;
    private final File archive;

    public Task1(String filename) {
        this.file = new File(filename);
        this.archive = new File(filename + ".zip");
    }

    public void run() {
        log.info("Обработка файла " + file);
        int totalSize = 0;
        List<Symbol> symbols = new ArrayList<>();
        try (InputStream stream = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = stream.read(buffer, 0, buffer.length)) >= 0) {
                totalSize += read;
                symbols.add(new Symbol(buffer, 0, read));
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        byte[] text = new byte[totalSize];
        int offset = 0;
        for (Symbol symbol : symbols) {
            System.arraycopy(symbol.bytes, 0, text, offset, symbol.bytes.length);
            offset += symbol.bytes.length;
        }
        log.info("Размер файла =  " + totalSize + " байт");

        List<Result> results = new ArrayList<>();
        Map<Integer, List<Point>> points = new HashMap<>();
        for (int n : WINDOW_SIZES) {
            log.debug("Размер последовательности = " + n);
            int countBlocks = totalSize - n + 1;
            Map<Symbol, Double> probabilities = new HashMap<>();
            for (int i = 0; i < countBlocks; ++i) {
                Symbol s = new Symbol(Arrays.copyOfRange(text, i, i + n), 0, n);
                probabilities.compute(s, (key, oldValue) -> {
                    if (oldValue == null) {
                        return 1.0;
                    }
                    return oldValue + 1;
                });
            }
            log.debug("Количество встретившихся последовательностей = " + probabilities.size());
            // количество не встречающихся символов алфавита
            double missing = Math.pow(ALPHABET_SIZE, n) - probabilities.size();
            // доля вероятности приходящаяся на эти символы
            double missingProbability = missing / Math.pow(totalSize, n);
            // коэффициент нормировки для оставшихся вероятностей
            double uncutMultiplier = 1 - missingProbability;

            Map<Symbol, Double> uncutProbabilities = new HashMap<>();
            for (Map.Entry<Symbol, Double> e : probabilities.entrySet()) {
                Symbol key = e.getKey();
                double value = e.getValue();
                probabilities.put(key, value / countBlocks);
                uncutProbabilities.put(key, value / countBlocks * uncutMultiplier);
            }

            double entropy = 0.0;
            for (double i : probabilities.values()) {
                entropy -= i * log(i, 2);
            }

            double uncutEntropy = 0.0;
            for (double i : uncutProbabilities.values()) {
                uncutEntropy -= i * log(i, 2);
            }
            uncutEntropy += missingProbability * log(Math.pow(totalSize, n), 2);

            entropy /= n;
            uncutEntropy /= n;

            Result result = new Result(n, entropy, uncutEntropy, (uncutEntropy * totalSize) / 8);
            results.add(result);

            log.info(String.format("%d | %.5f | %.5f | %6.0f bytes", result.getN(), result.getEntropy(), result.getNorm_entropy(), result.getMinEstimatedSize()));

            List<Double> sorted = new ArrayList<>(probabilities.values());
            Collections.sort(sorted);
            List<Point> pointsN = new ArrayList<>();
            for (int i = 0; i < sorted.size(); ++i) {
                Point point = new Point(sorted.get(i), 1d * (i + 1) / sorted.size());
                pointsN.add(point);
            }
            points.put(n, pointsN);
        }

        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new ClassTemplateLoader());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task1.html"))) {
            Template template = configuration.getTemplate("template/report.ftl", Locale.US);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("results", results);
            for (int i : WINDOW_SIZES) {
                parameters.put("points" + i, points.get(i).subList(0, points.get(i).size()));
            }
            parameters.put("file", file);
            parameters.put("archive", archive);
            template.process(parameters, writer);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.exit(1);
        }
        Locale.setDefault(Locale.US);
        new Task1(args[0]).run();
    }

    @Data
    @AllArgsConstructor
    public static class Point {
        private double x;
        private double y;

        public String toString() {
            return "{\"x\": " + x + ", \"y\": " + y + "}";
        }
    }

    @Data
    @AllArgsConstructor
    public static class Result {
        private int n;
        private double entropy;
        private double norm_entropy;
        private double minEstimatedSize;
    }

    /**
     * Возвращает логарифим параметра {@code x} по основанию {@code i}
     *
     * @param arg  параметр
     * @param base основание логарифма
     * @return результат функции
     */
    private static double log(double arg, double base) {
        return Math.log(arg) / Math.log(base);
    }

    /**
     * Класс-обертка для последовательностей байт
     */
    private class Symbol {
        private final byte[] bytes;
        private final int hash;

        public Symbol(byte[] bytes, int offset, int length) {
            this.bytes = Arrays.copyOfRange(bytes, offset, offset + length);
            this.hash = Arrays.hashCode(this.bytes);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Symbol) {
                Symbol another = (Symbol) obj;
                return Arrays.equals(this.bytes, another.bytes);
            }
            return false;
        }
    }
}