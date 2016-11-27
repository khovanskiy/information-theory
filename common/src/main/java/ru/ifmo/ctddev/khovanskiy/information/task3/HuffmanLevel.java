package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

/**
 * victor
 */
@Data
public class HuffmanLevel implements Comparable<HuffmanLevel> {
    private int ordinal;
    private int numberOfNodes;
    private int numberOfLeafs;
    private int bits;

    public HuffmanLevel(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public int compareTo(HuffmanLevel o) {
        return Integer.compare(this.ordinal, o.ordinal);
    }
}
