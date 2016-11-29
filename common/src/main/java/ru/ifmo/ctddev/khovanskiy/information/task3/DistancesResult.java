package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * victor
 */
@Data
@NoArgsConstructor
public class DistancesResult extends AlgorithmResult {
    private List<DistancesStepResult> stepResults;
}
