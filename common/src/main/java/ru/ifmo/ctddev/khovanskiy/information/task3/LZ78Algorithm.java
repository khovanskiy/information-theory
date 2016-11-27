package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class LZ78Algorithm extends Algorithm {

    @Override
    public LZ78Result encode(String input, boolean showDebugInfo) {
        LZ78Result result = new LZ78Result();
        input = Utils.convertToAscii(input);
        char[] x = input.toCharArray();
        int n = x.length;

        // Инициализация
        int N = 0;
        // Итоговое сообщение
        List<Integer> code = new ArrayList<>();
        Map<String, Integer> dictionary = new TreeMap<>();
        int wordNumber = 0;
        dictionary.put("", wordNumber++);
        List<LZ78StepResult> stepResults = new ArrayList<>();
        int ordinal = 1;
        while (N < n) {
            int l = 0;
            int targetWordNumber = 0;
            while (l + N <= input.length()) {
                boolean found = false;
                for (String word : dictionary.keySet()) {
                    if (word.equals(input.substring(N, N + l))) {
                        found = true;
                        targetWordNumber = dictionary.get(word);
                        break;
                    }
                }
                if (!found || l + N == n) {
                    break;
                }
                ++l;
            }

            LZ78StepResult stepResult = new LZ78StepResult();
            stepResult.setOrdinal(ordinal);
            String sequence = input.substring(N, N + l);
            stepResult.setSequence(sequence);
            stepResult.setWordNumber(targetWordNumber);
            String c = Utils.convertIntToBinaryString(targetWordNumber, Math.max(0, dictionary.size() - 2)) + (targetWordNumber == 0 ? "bin(" + x[N + l - 1] + ")" : "");
            stepResult.setCode(c);
            int bits = Utils.getBitsCount(Math.max(0, dictionary.size() - 2)) + (targetWordNumber == 0 ? Utils.charTo01List(x[N + l - 1]).size() : 0);
            stepResult.setBits(bits);

            stepResults.add(stepResult);

            code.addAll(Utils.convertIntToBinaryList(targetWordNumber, Math.max(0, dictionary.size() - 2)));
            if (targetWordNumber == 0) {
                code.addAll(Utils.charTo01List(x[N + l - 1]));
            }

            dictionary.put(input.substring(N, N + l), wordNumber++);
            if (l == 1) {
                N++;
            } else {
                N += l - 1;
            }
            ++ordinal;
        }
        result.setStepResults(stepResults);

        if (log.isInfoEnabled()) {
            log.info("Шаг\tСловарь\tНомер слова\tКодовые символы\t\tЗатраты");
            for (LZ78StepResult sr : stepResults) {
                log.info(sr.getOrdinal() + "\t" + sr.getSequence() + "\t\t" + sr.getWordNumber() + "\t\t\t" + sr.getCode() + "\t\t" + sr.getBits());
            }
            log.info("Итого = " + code.size() + " бит");
        }
        return result;
    }
}