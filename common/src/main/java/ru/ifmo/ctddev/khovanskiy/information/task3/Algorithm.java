package ru.ifmo.ctddev.khovanskiy.information.task3;

/**
 * @author Victor Khovanskiy
 */
public abstract class Algorithm {
    /**
     * Размер алфавита
     */
    protected static final int M = 256;

    public abstract AlgorithmResult encode(String source1, String source2, boolean showDebugInfo);
}
