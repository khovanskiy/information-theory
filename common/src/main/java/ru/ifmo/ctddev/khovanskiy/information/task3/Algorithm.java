package ru.ifmo.ctddev.khovanskiy.information.task3;

import java.io.PrintStream;
import java.util.List;

/**
 * @author victor
 */
public abstract class Algorithm {
    protected static final int M = 256;
    protected PrintStream out = System.out;

    public List<Integer> encode(String input) {
        return encode(input, false);
    }

    public abstract List<Integer> encode(String input, boolean showDebugInfo);
}
