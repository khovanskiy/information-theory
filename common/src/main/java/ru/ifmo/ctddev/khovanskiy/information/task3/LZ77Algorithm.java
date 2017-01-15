package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class LZ77Algorithm extends Algorithm {
    /**
     * Длина "окна"
     */
    private static final int W = 100;

    @Override
    public AlgorithmResult encode(String source1, String source2) {
        LZ77Result result = new LZ77Result();
        String input = Utils.convertToAscii(source1);
        result.setInput(source2);
        char[] sx = source2.toCharArray();
        char[] x = input.toCharArray();
        int n = x.length;
        // Инициализация
        int N = 0;
        List<Integer> code = new ArrayList<>();
        List<LZ77StepResult> stepResults = new ArrayList<>();
        int ordinal = 1;
        while (N < n) {
            // Находим l
            int l = 0;
            int d = 0;
            for (int i = Math.max(0, N - W); i < N; ++i) {
                int seen = i;
                int buffer = N;
                while (buffer < n && x[buffer] == x[seen]) {
                    buffer++;
                    seen++;
                }
                if (buffer - N >= l) {
                    l = buffer - N;
                    d = N - i - 1;
                }
            }

            LZ77StepResult stepResult = new LZ77StepResult();
            stepResult.setOrdinal(ordinal);
            stepResult.setFound(l != 0);

            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(sx[N]);
            for (int i = N + 1; i < N + l; i++) {
                sb.append(sx[i]);
            }
            sb.append("\"");
            stepResult.setSequence(sb.toString());

            String ds;
            if (l == 0) {
                ds = "-";
            } else {
                ds = d + "(" + N + ")";
            }
            stepResult.setD(ds);

            stepResult.setL(l);

            sb = new StringBuilder();
            if (l == 0) {
                sb.append("bin(").append(sx[N]).append(")");
            } else {
                sb.append(Utils.convertIntToBinaryString(d, N)).append(" ").append(Utils.convertIntListToString(Utils.getMonotoneCode(l)));
            }
            stepResult.setCode(sb.toString());

            int bits;
            if (l == 0) {
                bits = Utils.charTo01List(x[N]).size() + 1;
            } else {
                bits = 1 + Utils.getMonotoneCode(l).size() + Utils.convertIntToBinaryString(d, N).length();
            }
            stepResult.setBits(bits);

            stepResults.add(stepResult);

            if (l > 0) {
                // Последовательность найдена
                // К кодовому слову дописывается флаг 1
                code.add(1);
                // Расстояние до образца d в виде двоичной последовательности
                code.addAll(Utils.convertIntToBinaryList(d, N));
                // Длина совпадения в виде слова неравномерного префиксного кода
                code.addAll(Utils.getMonotoneCode(l));
                N += l;
            } else {
                // Последовательность не найдена
                // К кодовому слову дописывается флаг 0
                code.add(0);
                // Новая буква в виде двоичной последовательности
                code.addAll(Utils.charTo01List(x[N]));
                N++;
            }
            ++ordinal;
        }
        result.setStepResults(stepResults);
        result.setBits(stepResults.stream().mapToInt(LZ77StepResult::getBits).sum());
        if (log.isInfoEnabled()) {
            log.info("Итого = " + code.size() + " бит");
        }
        return result;
    }

}