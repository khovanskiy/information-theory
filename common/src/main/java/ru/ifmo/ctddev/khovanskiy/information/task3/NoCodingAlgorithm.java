package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

/**
 * victor
 */
@Slf4j
public class NoCodingAlgorithm extends Algorithm {

    @Override
    public AlgorithmResult encode(String source1, String source2, boolean showDebugInfo) {
        NoCodingResult result = new NoCodingResult();
        String input  = Utils.convertToAscii(source1);
        result.setInput(source2);
        char[] x = input.toCharArray();
        result.setBits(x.length * 8);
        return result;
    }
}
