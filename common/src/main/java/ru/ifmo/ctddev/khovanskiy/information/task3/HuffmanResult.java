package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * victor
 */
@Data
@NoArgsConstructor
public class HuffmanResult extends AlgorithmResult {
    private int messageCost;
    private int treeCost;
    private List<HuffmanLevel> levels;
    private List<HuffmanStepResult> stepResults;
}
