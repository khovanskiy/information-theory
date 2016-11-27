package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

import java.util.List;

/**
 * victor
 */
@Data
public class PPMAResult extends AlgorithmResult {
    private int bits;
    private List<PPMAStepResult> stepResults;
}
