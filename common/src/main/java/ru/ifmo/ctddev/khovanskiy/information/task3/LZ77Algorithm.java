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
    public AlgorithmResult encode(String input, boolean showDebugInfo) {
        LZ77Result result = new LZ77Result();
        input = Utils.convertToAscii(input);
        result.setInput(input);
        if (showDebugInfo) {
            out.println("string = " + input);
            out.println("| Шаг | Флаг | Последовательность букв | d | l | Кодовая последовательность | Биты |");
            out.println("|:-|:-|:-|:-|:-|:-|:-|");
        }
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
                int curSeen = i;
                int curBuf = N;
                while (curBuf < n && x[curBuf] == x[curSeen]) {
                    curBuf++;
                    curSeen++;
                }
                if (curBuf - N >= l) {
                    l = curBuf - N;
                    d = N - i - 1;
                }
            }

            LZ77StepResult stepResult = new LZ77StepResult();
            if (showDebugInfo) {
                /////////////////////////////////////////////////////////////////////////////////////////
                out.print("|" + ordinal + " | ");
                stepResult.setOrdinal(ordinal);
                out.print(((l == 0) ? 0 : 1) + " | ");
                stepResult.setFound(l != 0);

                StringBuilder sb = new StringBuilder();
                sb.append("\"" + x[N]);
                for (int i = N + 1; i < N + l; i++) {
                    sb.append(x[i]);
                }
                sb.append("\"");
                out.print(String.format("%-10s", sb.toString()));
                stepResult.setSequence(sb.toString());

                String ds;
                if (l == 0) {
                    ds = "-";
                } else {
                    ds = d + "(" + N + ")";
                }
                stepResult.setD(ds);
                out.print(String.format("\t%-7s\t", ds));

                stepResult.setL(l);
                out.print(String.format("%-4d", l));

                out.print(" | " + ((l == 0) ? 0 : 1) + " ");

                sb = new StringBuilder();
                if (l == 0) {
                    sb.append("bin(" + x[N] + ")");
                } else {
                    sb.append(Utils.convertIntToBinaryString(d, N) + " " + Utils.convertIntListToString(Utils.getMonotoneCode(l)));
                }
                out.print(String.format("%-15s|", sb.toString()));
                stepResult.setCode(sb.toString());

                int bits;
                if (l == 0) {
                    bits = Utils.charTo01List(x[N]).size() + 1;
                } else {
                    bits = 1 + Utils.getMonotoneCode(l).size() + Utils.convertIntToBinaryString(d, N).length();
                }
                stepResult.setBits(bits);
                out.print(bits);
                out.println("|");

                /////////////////////////////////////////////////////////////////////////////////////////
            }
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
                code.addAll(Utils.charTo01List(x[N + 1]));
                N++;
            }
            ++ordinal;
        }
        result.setStepResults(stepResults);
        result.setBits(stepResults.stream().mapToInt(LZ77StepResult::getBits).sum());
        if (showDebugInfo) {
            out.println("Итого = " + code.size() + " бит");
        }
        return result;
    }

}