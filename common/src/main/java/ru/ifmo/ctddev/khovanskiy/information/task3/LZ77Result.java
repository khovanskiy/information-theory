package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

import java.util.List;

/**
 * victor
 */
@Data
public class LZ77Result extends AlgorithmResult {
    private int bits;
    private List<LZ77StepResult> stepResults;
}
