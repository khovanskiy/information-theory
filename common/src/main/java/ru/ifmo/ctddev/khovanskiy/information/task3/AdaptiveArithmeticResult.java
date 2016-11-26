package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * victor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdaptiveArithmeticResult {
    private String input;
    private String output;
    private List<AdaptiveArithmeticStepResult> stepResults;
}
