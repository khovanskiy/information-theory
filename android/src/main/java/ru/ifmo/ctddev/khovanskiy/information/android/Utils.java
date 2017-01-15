package ru.ifmo.ctddev.khovanskiy.information.android;

import android.text.TextUtils;
import android.util.Pair;
import org.apache.commons.math3.fraction.Fraction;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author warrior
 */
public class Utils {

    private static final BigDecimal TWO = new BigDecimal(2);

    public static double entropy(List<Fraction> fractions) {
        if (!checkProbabilities(fractions)) {
            throw new IllegalArgumentException();
        }
        double entropy = 0;
        for (Fraction f : fractions) {
            double value = f.doubleValue();
            entropy += value * Math.log(value) / Math.log(2);
        }
        return -entropy;
    }

    public static Fraction binaryEntropy(List<Fraction> fractions) {
        if (!checkBinary(fractions) || !checkProbabilities(fractions)) {
            throw new IllegalArgumentException();
        }
        Fraction entropy = Fraction.ZERO;
        for (Fraction f : fractions) {
            entropy = entropy.add(f.multiply(power(f.getDenominator())));
        }
        return entropy;
    }

    public static Map<String, String> huffman(Map<String, Fraction> probabilities) {
        if (probabilities.size() == 1) {
            Map<String, String> result = new HashMap<>();
            result.put(probabilities.keySet().iterator().next(), "");
            return result;
        }
        String symbolMin = null;
        String symbolNextMin = null;
        Fraction probabilityMin = Fraction.TWO;
        Fraction probabilityNextMin = Fraction.TWO;
        for (String symbol : probabilities.keySet()) {
            Fraction probability = probabilities.get(symbol);
            if (probability.compareTo(probabilityMin) == -1) {
                probabilityNextMin = probabilityMin;
                symbolNextMin = symbolMin;
                probabilityMin = probability;
                symbolMin = symbol;
            } else if (probability.compareTo(probabilityNextMin) == -1) {
                probabilityNextMin = probability;
                symbolNextMin = symbol;
            }
        }
        Map<String, Fraction> newProbabilities = new HashMap<>(probabilities);
        newProbabilities.remove(symbolMin);
        newProbabilities.remove(symbolNextMin);
        String newSymbol = symbolMin + symbolNextMin;
        newProbabilities.put(newSymbol, probabilityMin.add(probabilityNextMin));
        Map<String, String> codeMap = huffman(newProbabilities);
        String code = codeMap.remove(newSymbol);
        codeMap.put(symbolMin, code + "0");
        codeMap.put(symbolNextMin, code + "1");
        return codeMap;
    }

    public static Map<String, String> shennon(Map<String, Fraction> probabilities) {
        List<Pair<String, BigDecimal>> listProbabilities = new ArrayList<>(probabilities.size());
        for (Map.Entry<String, Fraction> entry : probabilities.entrySet()) {
            listProbabilities.add(Pair.create(entry.getKey(),
                    new BigDecimal(entry.getValue().getNumerator()).divide(new BigDecimal(entry.getValue().getDenominator()))));
        }
        Collections.sort(listProbabilities, new Comparator<Pair<String, BigDecimal>>() {
            @Override
            public int compare(Pair<String, BigDecimal> lhs, Pair<String, BigDecimal> rhs) {
                return rhs.second.compareTo(lhs.second);
            }
        });

        BigDecimal[] q = new BigDecimal[probabilities.size()];
        q[0] = BigDecimal.ZERO;
        for (int i = 1; i < q.length; i++) {
            q[i] = q[i - 1].add(listProbabilities.get(i - 1).second);
        }
        Map<String, String> code = new HashMap<>(probabilities.size());
        for (int i = 0; i < listProbabilities.size(); i++) {
            Pair<String, BigDecimal> pair = listProbabilities.get(i);
            int digits = (int) Math.ceil(-Math.log(pair.second.doubleValue()) / Math.log(2));
            code.put(pair.first, toBinaryString(q[i], digits));
        }
        return code;
    }

    public static Map<String, String> gilbert(LinkedHashMap<String, Fraction> probabilities) {
        List<Pair<String, BigDecimal>> listProbabilities = new ArrayList<>(probabilities.size());
        for (Map.Entry<String, Fraction> entry : probabilities.entrySet()) {
            listProbabilities.add(Pair.create(entry.getKey(),
                    new BigDecimal(entry.getValue().getNumerator()).divide(new BigDecimal(entry.getValue().getDenominator()))));
        }

        BigDecimal[] q = new BigDecimal[probabilities.size()];
        BigDecimal[] sigma = new BigDecimal[probabilities.size()];
        q[0] = BigDecimal.ZERO;
        sigma[0] = listProbabilities.get(0).second.divide(TWO);
        for (int i = 1; i < q.length; i++) {
            q[i] = q[i - 1].add(listProbabilities.get(i - 1).second);
            sigma[i] = q[i].add(listProbabilities.get(i).second.divide(TWO));
        }
        Map<String, String> code = new HashMap<>(probabilities.size());
        for (int i = 0; i < listProbabilities.size(); i++) {
            Pair<String, BigDecimal> pair = listProbabilities.get(i);
            int digits = (int) Math.ceil(-Math.log(listProbabilities.get(i).second.doubleValue()) / Math.log(2)) + 1;
            code.put(pair.first, toBinaryString(sigma[i], digits));
        }
        return code;
    }

    public static String arithmeticCoding(LinkedHashMap<String, Fraction> probabilities, String str) {
        List<Pair<String, BigDecimal>> listProbabilities = new ArrayList<>(probabilities.size());
        Map<String, Integer> indexes = new HashMap<>();
        int k = 0;
        for (Map.Entry<String, Fraction> entry : probabilities.entrySet()) {
            listProbabilities.add(Pair.create(entry.getKey(),
                    new BigDecimal(entry.getValue().getNumerator()).divide(new BigDecimal(entry.getValue().getDenominator()))));
            indexes.put(entry.getKey(), k);
            k++;
        }

        BigDecimal[] q = new BigDecimal[probabilities.size()];
        q[0] = BigDecimal.ZERO;
        for (int i = 1; i < q.length; i++) {
            q[i] = q[i - 1].add(listProbabilities.get(i - 1).second);
        }
        BigDecimal f = BigDecimal.ZERO;
        BigDecimal g = BigDecimal.ONE;
        for (int i = 0; i < str.length(); i++) {
            String symbol = String.valueOf(str.charAt(i));
            int index = indexes.get(symbol);
            f = f.add(q[index].multiply(g));
            g = g.multiply(listProbabilities.get(index).second);
        }
        int digits = (int) Math.ceil(-Math.log(g.doubleValue()) / Math.log(2)) + 1;
        return toBinaryString(f.add(g.divide(TWO)), digits);
    }

    public static String arithmeticDecoding(LinkedHashMap<String, Fraction> probabilities, String fValue, int len) {
        List<Pair<String, BigDecimal>> listProbabilities = new ArrayList<>(probabilities.size());
        Map<String, Integer> indexes = new HashMap<>();
        int k = 0;
        for (Map.Entry<String, Fraction> entry : probabilities.entrySet()) {
            listProbabilities.add(Pair.create(entry.getKey(),
                    new BigDecimal(entry.getValue().getNumerator()).divide(new BigDecimal(entry.getValue().getDenominator()))));
            indexes.put(entry.getKey(), k);
            k++;
        }

        BigDecimal[] q = new BigDecimal[probabilities.size() + 1];
        q[0] = BigDecimal.ZERO;
        for (int i = 1; i < q.length - 1; i++) {
            q[i] = q[i - 1].add(listProbabilities.get(i - 1).second);
        }
        q[q.length - 1] = BigDecimal.ONE;
        BigDecimal s = BigDecimal.ZERO;
        BigDecimal g = BigDecimal.ONE;
        BigDecimal f = toDecimalValue(fValue);
        String result = "";
        for (int i = 0; i < len; i++) {
            int j = 0;
            while (s.add(q[j + 1].multiply(g)).compareTo(f) == -1) {
                j++;
            }
            s = s.add(q[j].multiply(g));
            String letter = "" + (char) ('a' + j);
            g = g.multiply(listProbabilities.get(indexes.get(letter)).second);
            result += letter;
        }
        return result;
    }

    public static BigDecimal toDecimalValue(String binaryString) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal two = new BigDecimal(2);
        for (int i = 1; i <= binaryString.length(); i++) {
            BigDecimal cur = new BigDecimal(binaryString.charAt(i - 1) - '0');
            result = result.add(BigDecimal.ONE.divide(two.pow(i)).multiply(cur));
        }
        return result;
    }

    public static String toBinaryString(BigDecimal value, int digits) {
        String result = "";
        BigDecimal two = new BigDecimal(2);
        for (int i = 0; i < digits; i++) {
            value = value.multiply(two);
            if (value.compareTo(BigDecimal.ONE) >= 0) {
                result += "1";
                value = value.subtract(BigDecimal.ONE);
            } else {
                result += "0";
            }
        }
        return result;
    }

    public static Fraction averageLength(Map<String, Fraction> probabilities, Map<String, String> code) {
        if (!probabilities.keySet().equals(code.keySet())) {
            throw new IllegalArgumentException("!probabilities.keySet().equals(code.keySet())");
        }
        Fraction averageLength = Fraction.ZERO;
        for (String symbol : probabilities.keySet()) {
            averageLength = averageLength.add(probabilities.get(symbol).multiply(code.get(symbol).length()));
        }
        return averageLength;
    }

    public static String fractionToString(Fraction f) {
        return f.getNumerator() + "/" + f.getDenominator() + " [" + f.doubleValue() + "]";
    }

    public static String codeToString(List<String> symbols, Map<String, String> code) {
        List<String> codes = new ArrayList<>(symbols.size());
        for (String s : symbols) {
            codes.add(code.get(s));
        }
        return TextUtils.join(", ", codes);
    }

    public static int power(int i) {
        int power = 0;
        while (i % 2 == 0) {
            power++;
            i /= 2;
        }
        return power;
    }

    private static boolean checkProbabilities(List<Fraction> fractions) {
        Fraction sum = Fraction.ZERO;
        for (Fraction f : fractions) {
            sum = sum.add(f);
        }
        return sum.compareTo(Fraction.ONE) == 0;
    }

    private static boolean checkBinary(List<Fraction> fractions) {
        for (Fraction f : fractions) {
            int num = f.getNumerator();
            int den = f.getDenominator();
            if (num != 1 || !isPowerOfTwo(den)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPowerOfTwo(int i) {
        while (i % 2 == 0) {
            i /= 2;
        }
        return i == 1;
    }

    /**
     * Возвращает логарифим параметра {@code x} по основанию {@code i}
     *
     * @param arg  параметр
     * @param base основание логарифма
     * @return результат функции
     */
    public static double log(double arg, double base) {
        return Math.log(arg) / Math.log(base);
    }

    public static double informationalCapacity(double p, double e) {
        p /= e;
        double q = 1 - p;
        double n = (-p * log(p, 2) - q * log(q, 2));
        return (1 - e) * n;
    }
}
