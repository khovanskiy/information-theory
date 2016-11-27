package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * victor
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NumericResult extends AlgorithmResult {
    /**
     * Количество различных композиций
     */
    private BigInteger numberOfCompositions;
    /**
     * Длина части кодового слова для передачи номера композиции
     */
    private int l1;
    /**
     * Номер композиции
     */
    private BigInteger positionOfComposition;
    /**
     * Количество последовательностей с фиксированной композицией
     */
    private BigInteger numberOfSequences;
    /**
     * Длина части кодового слова для передачи номера композиции
     */
    private int l2;
    /**
     * Номер последовательности
     */
    private BigInteger positionOfSequence;

}
