package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Victor Khovanskiy
 */
@Data
@NoArgsConstructor
public class PPMAStepResult {
    private int ordinal;
    private String symbol;
    private String context;
    private String tau;
    private String pescs;
    private String pac;
}
