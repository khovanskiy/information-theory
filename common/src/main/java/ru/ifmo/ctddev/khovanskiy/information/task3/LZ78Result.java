package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

import java.util.List;

/**
 * Victor Khovanskiy
 */
@Data
public class LZ78Result extends AlgorithmResult {
    private int bits;
    private List<LZ78StepResult> stepResults;
}
