package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * victor
 */
@Data
@NoArgsConstructor
public class HuffmanStepResult {
    /**
     * Символ
     */
    private String symbol;
    /**
     * Число появлений
     */
    private int numberOfOccurrence;
    /**
     * Кодовое слово
     */
    private String code;
}
