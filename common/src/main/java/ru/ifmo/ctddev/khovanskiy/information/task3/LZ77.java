package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class LZ77 extends Algorithm {
    private static final int W = 100;

    @Override
    public List<Integer> encode(String inputString, boolean showDebugInfo) {
        if (showDebugInfo) {
//            out.println("string = " + inputString);
            out.println("| Шаг | Флаг | Последовательность букв | d | l | Кодовая последовательность | Биты |");
            out.println("|:-|:-|:-|:-|:-|:-|:-|");
        }
        char[] input = inputString.toCharArray();
        int n = 0;
        List<Integer> code = new ArrayList<>();
        int stepNumber = 1;
        while (n < input.length) {

            int l = 0;
            int d = 0;
            for (int i = Math.max(0, n - W); i < n; i++) {
                int curSeen = i, curBuf = n;
                while (curBuf < input.length && input[curBuf] == input[curSeen]) {
                    curBuf++;
                    curSeen++;
                }
                if (curBuf - n >= l) {
                    l = curBuf - n;
                    d = n - i - 1;
                }
            }

            if (showDebugInfo) {
                /////////////////////////////////////////////////////////////////////////////////////////
                out.print("|" + stepNumber + " | ");
                out.print(((l == 0) ? 0 : 1) + " | ");

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\"" + input[n]);
                for (int i = n + 1; i < n + l; i++) {
                    stringBuilder.append(input[i]);
                }
                stringBuilder.append("\"");
                out.print(String.format("%-10s", stringBuilder.toString()));
                if (l == 0) {
                    out.print(String.format(" | %-7s |", "-"));
                } else {
                    out.print(String.format(" | %-7s |", d + "(" + n + ")"));
                }
                out.print(String.format("%-4d", l));
                out.print(" | " + ((l == 0) ? 0 : 1) + " ");

                stringBuilder = new StringBuilder();
                if (l == 0) {
                    stringBuilder.append("bin(" + input[n] + ")");
                } else {
                    stringBuilder.append(Utils.convertIntToBinaryString(d, n) + " " + Utils.convertIntListToString(Utils.getMonotoneCode(l)));
                }
                out.print(String.format("%-15s|", stringBuilder.toString()));
                if (l == 0) {
                    out.print(Utils.charTo01List(input[n]).size() + 1);
                } else {
                    out.print(1 + Utils.getMonotoneCode(l).size() + Utils.convertIntToBinaryString(d, n).length());
                }
                out.println("|");

                /////////////////////////////////////////////////////////////////////////////////////////
            }

            if (l > 0) {
                code.add(1);
                code.addAll(Utils.convertIntToBinaryList(d, n));
                code.addAll(Utils.getMonotoneCode(l));
                n += l;
            } else {
                code.add(0);
                code.addAll(Utils.charTo01List(input[n]));
                n++;
            }
            stepNumber++;
        }
        if (showDebugInfo) {
            out.println("Итого = " + code.size() + " бит");
        }
        return code;
    }

}