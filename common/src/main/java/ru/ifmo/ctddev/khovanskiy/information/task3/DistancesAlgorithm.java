package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class DistancesAlgorithm extends Algorithm {

    /**
     * Форматирует и возвращает последовательность в виде HTML-кода
     *
     * @param sequence  последовательность
     * @param positions индексы
     * @return строка
     */
    public static String formatSequence(char[] sequence, int[] positions) {
        String buffer = "";
        boolean[] aim = new boolean[sequence.length];
        for (int idx : positions)
            aim[idx] = true;
        for (int i = 0; i < sequence.length; i++) {
            if (aim[i])
                buffer += "<span style=\"background-color:yellow\">";
            buffer += sequence[i];
            if (aim[i])
                buffer += "</span>";
        }
        return buffer;
    }

    @Override
    public AlgorithmResult encode(String source1, String source2) {
        DistancesResult result = new DistancesResult();
        String input = source2;
        result.setInput(source2);
        char[] sequence = new char[input.length()];
        Arrays.fill(sequence, '?');
        int filled = 0;
        for (int i = 0; i < input.length(); i++) {
            if (!input.substring(0, i).contains(input.substring(i, i + 1))) {
                sequence[i] = input.charAt(i);
                filled++;
            }
        }
        List<DistancesStepResult> stepResults = new ArrayList<>();
        int ordinal = 0;
        int letter = 0;
        while (letter < sequence.length) {
            DistancesStepResult stepResult = new DistancesStepResult();
            stepResult.setOrdinal(ordinal);

            int questions = 0;
            int pos = letter + 1;
            while (pos < input.length() && sequence[pos] == '?') {
                pos++;
            }
            String substring = input.substring(pos);
            int index = substring.indexOf(input.substring(letter, letter + 1));
            String y = "";
            if (index != -1) {
                index += pos;
                for (int i = pos; i <= index; ++i) {
                    if (sequence[i] == '?') {
                        ++questions;
                    }
                }

                String formattedSequence = formatSequence(sequence, new int[]{letter, index});
                stepResult.setSequence(formattedSequence);
                y += questions;

                for (int i = index + 1; i < sequence.length; ++i) {
                    if (sequence[i] == '?') {
                        questions++;
                    }
                }
                y += "(" + questions + ")";

                sequence[index] = sequence[letter];
                filled++;
            } else {
                String formattedSequence = formatSequence(sequence, new int[]{letter});
                stepResult.setSequence(formattedSequence);
                if (filled == sequence.length) {
                    stepResult.setY("");
                    stepResults.add(stepResult);
                    break;
                } else {
                    y = "0";
                }
            }
            stepResult.setY(y);

            int position = letter + 1;
            while (position < sequence.length && sequence[position] == '?') {
                sequence[position++] = sequence[letter];
                filled++;
            }
            letter = position;
            stepResults.add(stepResult);
            ++ordinal;
        }
        result.setStepResults(stepResults);
        return result;
    }
}
