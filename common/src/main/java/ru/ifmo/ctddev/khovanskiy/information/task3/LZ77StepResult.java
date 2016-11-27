package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

/**
 * victor
 */
@Data
public class LZ77StepResult {
    private int ordinal;
    private boolean found;
    private String sequence;
    private String d;
    private int l;
    private String code;
    private int bits;
}
