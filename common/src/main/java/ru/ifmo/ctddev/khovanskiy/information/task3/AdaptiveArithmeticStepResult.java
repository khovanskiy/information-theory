package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;

import java.math.BigDecimal;

/**
 * victor
 */
@Data
public class AdaptiveArithmeticStepResult {
    private int ordinal;
    private char x;
    private double p;
    private BigDecimal q;
    private BigDecimal F;
    private BigDecimal G;
}
