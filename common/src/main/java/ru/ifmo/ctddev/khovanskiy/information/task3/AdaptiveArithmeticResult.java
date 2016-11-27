package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * victor
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdaptiveArithmeticResult extends AlgorithmResult {
    private int bits;
    private List<AdaptiveArithmeticStepResult> stepResults;
}
