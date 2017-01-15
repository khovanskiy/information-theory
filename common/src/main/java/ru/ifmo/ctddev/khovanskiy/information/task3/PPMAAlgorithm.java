package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class PPMAAlgorithm extends Algorithm {
    /**
     * Параметр
     */
    public static final int D = 5;

    public static final double EPSILON = 1e-9;

    public static final int MAX = 100000;

    /**
     * Вычисляет и возвращает наибольший общий делитель
     *
     * @return наибольший общий делитель
     */
    private static int gcd(int x, int y) {
        return x == 0 ? y : gcd(y % x, x);
    }

    /**
     * Вычисляет и возвращает количество вхождений подстроки в в строку
     *
     * @param s строка
     * @param t подстрока
     * @return количество вхождений
     */
    private int occurrences(String s, String t) {
        int total = 0;
        int sl = s.length();
        int tl = t.length();
        for (int p = 0; p + tl <= sl; ++p) {
            if (s.substring(p, p + tl).equals(t)) {
                ++total;
            }
        }
        return total;
    }

    @Override
    public AlgorithmResult encode(String source1, String source2) {
        PPMAResult result = new PPMAResult();
        String input = source2;
        result.setInput(input);
        char[] x = input.toCharArray();
        int n = x.length;
        int unused = 1 << 8;
        String buffer = "";
        List<PPMAStepResult> stepResults = new ArrayList<>();
        double tEsc = 0;
        double tSymbol = 0;
        for (int i = 0; i < n; ++i) {
            PPMAStepResult stepResult = new PPMAStepResult();
            stepResult.setOrdinal(i);
            String symbol = x[i] + "";
            stepResult.setSymbol(symbol);

            // Находим наибольшее d, удовлетворяющее условию
            int d = 0;
            while (i - d >= 0 && d <= D && (buffer.length() == 0 || buffer.substring(0, buffer.length() - 1).contains(input.substring(i - d, i)))) {
                d++;
            }
            d--;
            // Выбираем контекст
            String context = input.substring(i - d, i);
            stepResult.setContext(context);
            String tau = Integer.toString(occurrences(buffer, context) - 1);
            if (i == 0) {
                tau = "0";
            }
            double esc = 1;
            double symb = 1;
            HashSet<Character> available = new HashSet<>();
            while (d > 0) {
                if (buffer.contains(context + input.charAt(i))) {
                    break;
                }
                int remTmp = 0;
                for (Character c : available) {
                    remTmp += occurrences(buffer, context + c);
                }
                for (int startPos = 0; startPos + context.length() + 1 <= buffer.length(); startPos++) {
                    if (buffer.substring(startPos, startPos + context.length()).equals(context)) {
                        available.add(buffer.charAt(startPos + context.length()));
                    }
                }
                esc *= occurrences(buffer.substring(0, buffer.length() - 1), context) + 1 - remTmp;
                context = context.substring(1);
                // Уменьшаем длину контекста
                d--;
                tau += "," + Integer.toString(occurrences(buffer, context) - 1);
            }
            stepResult.setTau(tau);

            int z = 0;
            for (Character c : available) {
                z += occurrences(buffer, context + c);
            }
            if (d > 0) {
                // Кодируем x_{t+1} в соответствии с контекстом
                int withLetter = occurrences(buffer, context + x[i]);
                int total = occurrences(buffer.substring(0, buffer.length() - 1), context);
                symb *= (total + 1.0 - z) / withLetter;
            } else {
                // Кодирование без контекста
                if (buffer.indexOf(input.charAt(i)) != -1) {
                    int withoutLetter = occurrences(buffer, input.substring(i, i + 1));
                    int total = buffer.length();
                    symb *= (total + 1.0 - z) / withoutLetter;
                } else {
                    esc *= buffer.length() + 1 - z;
                    symb *= unused--;
                }
            }

            String pescs;
            if (esc != 1) {
                pescs = fraction(1 / esc);
            } else {
                pescs = " ";
            }
            stepResult.setPescs(pescs);

            String pas = fraction(1 / symb);
            stepResult.setPac(pas);

            tEsc += Utils.log(esc, 2);
            tSymbol += Utils.log(symb, 2);
            buffer += input.charAt(i);
            stepResults.add(stepResult);
        }
        result.setStepResults(stepResults);
        int bits = ((int) Math.ceil(tEsc + tSymbol) + 1);
        result.setBits(bits);

        log.info("Итого = up[ %.4f + %.4f ] + 1 = %d бит\n\n", tEsc, tSymbol, result.getBits());
        return result;
    }

    private static String fraction(double input) {
        int bestResult = 1;
        for (int value = 1; value <= MAX; ++value) {
            if (Math.abs(value * input - Math.round(value * input)) <= EPSILON) {
                bestResult = value;
            }
        }
        int bestResult2 = (int) Math.round(bestResult * input);
        int gcdResult = gcd(bestResult2, bestResult);
        bestResult /= gcdResult;
        bestResult2 /= gcdResult;
        return bestResult == 1 ? Integer.toString(bestResult2) : Integer.toString(bestResult2) + "/" + Integer.toString(bestResult);
    }
}
