package ru.ifmo.ctddev.khovanskiy.information.task3;

import java.io.PrintStream;
import java.util.List;

/**
 * @author victor
 */
public abstract class Algorithm {
    /**
     * Размер алфавита
     */
    protected static final int M = 256;
    protected PrintStream out = System.out;

    public AlgorithmResult encode(String input) {
        return encode(input, false);
    }

    public abstract AlgorithmResult encode(String input, boolean showDebugInfo);
}
