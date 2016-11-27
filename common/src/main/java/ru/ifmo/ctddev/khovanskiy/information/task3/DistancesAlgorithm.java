package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.Arrays;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class DistancesAlgorithm extends Algorithm {
    private static final String[] proverbs = {"CC######LLWWWWISNUUAANNHWDD#AAOOO#####OOEEDTESFDSE"};

    public static void print(char[] seq, int[] indices) {
        boolean[] aim = new boolean[seq.length];
        for (int idx : indices)
                aim[idx] = true;
        for (int i = 0; i < seq.length; i++) {
            if (aim[i])
                System.out.print("<span style=\"background-color:yellow\">");
            System.out.print(seq[i]);
            if (aim[i])
                System.out.print("</span>");
        }
    }

    @Override
    public AlgorithmResult encode(String input, boolean showDebugInfo) {
        DistancesResult result = new DistancesResult();
        input = Utils.convertToAscii(input);
        result.setInput(input);
        System.out.println("|i|Последовательность|y_i(y_i,max)|");
        System.out.println("|:-|:-|:-|");
        char[] seq = new char[input.length()];
        Arrays.fill(seq, '?');
        int filled = 0;
        for (int i = 0; i < input.length(); i++) {
            if (!input.substring(0, i).contains(input.substring(i, i + 1))) {
                seq[i] = input.charAt(i);
                filled++;
            }
        }
        int currentIteration = 0;
        int currentLetter = 0;
        while (currentLetter < seq.length) {
            System.out.print(currentIteration++ + "|");

            int countQuestions = 0;
            int st = currentLetter + 1;
            while (st < input.length() && seq[st] == '?') {
                st++;
            }
            int idx = input.substring(st).indexOf(input.substring(currentLetter, currentLetter + 1));
            if (idx != -1) {
                idx += st;
                for (int i = st; i <= idx; i++)
                    if (seq[i] == '?')
                        countQuestions++;

                print(seq, new int[]{currentLetter, idx});
                System.out.print("|" + countQuestions);

                for (int i = idx + 1; i < seq.length; i++)
                    if (seq[i] == '?')
                        countQuestions++;
                System.out.println("(" + countQuestions + ")");

                seq[idx] = seq[currentLetter];
                filled++;
            } else {
                print(seq, new int[]{currentLetter});
                if (filled == seq.length) {
                    break;
                } else {
                    System.out.print("|0");
                }
                System.out.println("|");
            }

            int let = currentLetter + 1;
            while (let < seq.length && seq[let] == '?') {
                seq[let++] = seq[currentLetter];
                filled++;
            }
            currentLetter = let;
        }
        return result;
    }

    /*public static void main(String[] args) {
        System.out.println("Кодирование расстояний");
        for (final String proverb : proverbs) {
            encode(proverb, true);
        }
    }*/
}
