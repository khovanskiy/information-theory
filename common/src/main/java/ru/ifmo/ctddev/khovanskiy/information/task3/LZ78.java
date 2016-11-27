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
public class LZ78 extends Algorithm {

    @Override
    public List<Integer> encode(String input, boolean showDebugInfo) {
        if (showDebugInfo) {
//            out.println("string = " + inputString);
            out.println("| Шаг | Словарь | Номер слова | Кодовые символы | Затраты|");
            out.println("|:-|:-|:-|:-|:-|");
        }
        int n = 0;
        List<Integer> code = new ArrayList<>();
        Map<String, Integer> dictionary = new TreeMap<>();
        int wordNumber = 0;
        dictionary.put("", wordNumber++);
        int stepNumber = 1;
        while (n < input.length()) {
            int l = 0;
            int targetWordNumber = 0;
            while (l + n <= input.length()) {
                boolean ok = false;
                for (String word : dictionary.keySet()) {
                    if (word.equals(input.substring(n, n + l))) {
                        ok = true;
                        targetWordNumber = dictionary.get(word);
                        break;
                    }
                }
                if (!ok || l + n == input.length()) {
                    break;
                }
                l++;
            }

            if (showDebugInfo) {
                /////////////////////////////////////////////////////////////////////////////////////////
                out.print(" | " + stepNumber++ + " | \"" + input.substring(n, n + l) + "\" | ");
                out.print(targetWordNumber + " | ");
                out.print(" " + Utils.convertIntToBinaryString(targetWordNumber, Math.max(0, dictionary.size() - 2)));
                out.print((targetWordNumber == 0 ? "bin(" + input.charAt(n + l - 1) + ")" : "") + " | ");
                out.println(Utils.getBitsCount(Math.max(0, dictionary.size() - 2)) + (targetWordNumber == 0 ? Utils.charTo01List(input.charAt(n + l - 1)).size() : 0) + " | ");
                /////////////////////////////////////////////////////////////////////////////////////////
            }

            code.addAll(Utils.convertIntToBinaryList(targetWordNumber, Math.max(0, dictionary.size() - 2)));
            if (targetWordNumber == 0) {
                code.addAll(Utils.charTo01List(input.charAt(n + l - 1)));
            }

            dictionary.put(input.substring(n, n + l), wordNumber++);
            if (l == 1) {
                n++;
            } else {
                n += l - 1;
            }
        }
        if (showDebugInfo) {
            out.println("Итого = " + code.size() + " бит");
        }
        return code;
    }
}