package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class PPMAAlgorithm extends Algorithm {
    /**
     * Параметр
     */
    public final static int D = 5;

    private static String genFraction(double x) {
        final int MAX_DENOM = 100000;
        final double eps = 1e-9;
        int bestDenom = 1;
        for (int denom = 1; denom <= MAX_DENOM; denom++) {
            if (Math.abs(denom * x - Math.round(denom * x)) <= eps) {
                bestDenom = denom;
            }
        }
        int bestNom = (int) Math.round(bestDenom * x);
        int g = gcd(bestNom, bestDenom);
        bestDenom /= g;
        bestNom /= g;
        return bestDenom == 1 ? Integer.toString(bestNom) : Integer.toString(bestNom) + "/" + Integer.toString(bestDenom);
    }

    /**
     * Вычисляет и возвращает наибольший общий делитель
     *
     * @return наибольший общий делитель
     */
    private static int gcd(int x, int y) {
        return x == 0 ? y : gcd(y % x, x);
    }

    private int countOccurrences(String s, String t) {
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
    public AlgorithmResult encode(String input, boolean showDebugInfo) {
        PPMAResult result = new PPMAResult();
        input = Utils.convertToAscii(input);
        result.setInput(input);
        char[] x = input.toCharArray();
        int n = x.length;

        log.info("Исходная строка = " + input + "\n");
        int unused = 1 << 8;
        String was = "";
        log.info("Шаг\tБуква\tКонтекст\ttau_s\tpr_esc\tpr_symbol");
        List<PPMAStepResult> stepResults = new ArrayList<>();
        double prEsc = 0;
        double prSymbol = 0;
        for (int i = 0; i < n; ++i) {
            PPMAStepResult sr = new PPMAStepResult();
            sr.setOrdinal(i);
            String symbol = x[i] + "";
            sr.setSymbol(symbol);
            System.out.print("|" + (sr.getOrdinal() + 1) + "|" + sr.getSymbol() + "|");

            // Находим наибольшее d, удовлетворяющее условию
            int d = 0;
            while (i - d >= 0 && d <= D && (was.length() == 0 || was.substring(0, was.length() - 1).contains(input.substring(i - d, i)))) {
                d++;
            }
            d--;
            // Выбираем контекст
            String context = input.substring(i - d, i);
            sr.setContext(context);
            if (context.length() == 0) {
                System.out.print("#");
            } else {
                for (int j = 0; j < context.length(); ++j) {
                    if (context.charAt(j) == ' ') {
                        System.out.print("\\_");
                    } else {
                        System.out.print(context.charAt(j));
                    }
                }
            }
            System.out.print("|||||");
            String tau = Integer.toString(countOccurrences(was, context) - 1);
            if (i == 0) {
                tau = "0";
            }
            double tmpPrEsc = 1;
            double tmpPrSymbol = 1;
            HashSet<Character> available = new HashSet<>();
            while (d > 0) {
                if (was.contains(context + input.charAt(i))) {
                    break;
                }
                int remTmp = 0;
                for (Character c : available) {
                    remTmp += countOccurrences(was, context + c);
                }
                for (int startPos = 0; startPos + context.length() + 1 <= was.length(); startPos++) {
                    if (was.substring(startPos, startPos + context.length()).equals(context)) {
                        available.add(was.charAt(startPos + context.length()));
                    }
                }
                tmpPrEsc *= countOccurrences(was.substring(0, was.length() - 1), context) + 1 - remTmp;
                context = context.substring(1);
                // Уменьшаем длину контекста
                d--;
                tau += "," + Integer.toString(countOccurrences(was, context) - 1);
            }
            sr.setTau(tau);
            log.info(sr.getTau() + "|");

            int rem = 0;
            for (Character c : available) {
                rem += countOccurrences(was, context + c);
            }
            if (d > 0) {
                // Кодируем x_{t+1} в соответствии с контекстом
                int cntWithCurrentLetter = countOccurrences(was, context + x[i]);
                int cntTotal = countOccurrences(was.substring(0, was.length() - 1), context);
                tmpPrSymbol *= (cntTotal + 1.0 - rem) / cntWithCurrentLetter;
            } else {
                // Кодирование без контекста
                if (was.indexOf(input.charAt(i)) != -1) {
                    int cntCurrentLetter = countOccurrences(was, input.substring(i, i + 1));
                    int cntTotal = was.length();
                    tmpPrSymbol *= (cntTotal + 1.0 - rem) / cntCurrentLetter;
                } else {
                    tmpPrEsc *= was.length() + 1 - rem; // esc-symbol
                    tmpPrSymbol *= unused--;
                }
            }

            String pescs;
            if (tmpPrEsc != 1) {
                pescs = genFraction(1 / tmpPrEsc);
            } else {
                pescs = " ";
            }
            sr.setPescs(pescs);

            String pas = genFraction(1 / tmpPrSymbol);
            sr.setPac(pas);

            System.out.println(pescs + "*****" + pas);
            prEsc += Utils.log(tmpPrEsc, 2);
            prSymbol += Utils.log(tmpPrSymbol, 2);
            was += input.charAt(i);
            stepResults.add(sr);
        }
        result.setStepResults(stepResults);
        int bits = ((int) Math.ceil(prEsc + prSymbol) + 1);
        result.setBits(bits);
        System.out.printf(Locale.US, "Итого = up[ %.4f + %.4f ] + 1 = %d бит\n\n", prEsc, prSymbol, result.getBits());
        return result;
    }

    /*public static void main(String[] args) {
        System.out.println("PPMA кодирование");
        for (final String proverb : proverbs) {
            encodePPMA(proverb);
        }
    }*/
}
