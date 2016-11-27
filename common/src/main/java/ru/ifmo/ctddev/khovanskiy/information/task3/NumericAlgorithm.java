package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class NumericAlgorithm extends Algorithm {

    @Override
    public NumericResult encode(String input, boolean showDebugInfo) {
        NumericResult result = new NumericResult();
        input = Utils.convertToAscii(input);
        result.setInput(input);
        char[] x = input.toCharArray();
        int n = x.length;

        // Композиция
        int[] tau = tau(x);

        // Количество различных композиций
        BigInteger numberOfCompositions = Ntau(M, n);
        result.setNumberOfCompositions(numberOfCompositions);
        // Длина части кодового слова для передачи номера композиции
        int l1 = (int) Math.ceil(Utils.log(numberOfCompositions.doubleValue(), 2));
        result.setL1(l1);
        // Номер композиции
        BigInteger positionOfComposition = positionOfComposition(tau, n);
        result.setPositionOfComposition(positionOfComposition);

        // Кодовое слово в бинарном виде
        List<Integer> code = new ArrayList<>();

        // Номер композиции в бинарном виде
        List<Integer> numberOfCompositionCode = Utils.convertStringToIntList(positionOfComposition.toString(2));
        // Заполняем ведущими нулями
        for (int i = 0; i < l1 - numberOfCompositionCode.size(); i++) {
            code.add(0);
        }
        //
        code.addAll(numberOfCompositionCode);

        // Количество последовательностей с фиксированной композицией
        BigInteger numberOfSequences = N(tau);
        result.setNumberOfSequences(numberOfSequences);
        // Длина части кодового слова для передачи номера последовательности
        int l2 = (int) Math.ceil(Utils.log(numberOfSequences.doubleValue(), 2));
        result.setL2(l2);
        // Номер последовательности
        BigInteger positionOfSequence = positionOfSequence(x);
        result.setPositionOfSequence(positionOfSequence);

        // Номер последовательности в бинарном виде
        List<Integer> numberOfSequenceCode = Utils.convertStringToIntList(positionOfSequence.toString(2));
        // Заполняем ведущими нулями
        for (int i = 0; i < l2 - numberOfSequenceCode.size(); i++) {
            code.add(0);
        }
        code.addAll(numberOfSequenceCode);

        // Кодовое слово
        String output = Utils.convertIntListToString(code);
        result.setOutput(output);

        if (log.isInfoEnabled()) {
            out.println("строка = " + input);
            out.println("количество композиций = " + numberOfCompositions);
            out.println("номер текущей композиции = " + positionOfComposition);
            out.println("количество битов для передачи композиции = " + l1);

            out.println("total number of sequences = " + numberOfSequences);
            out.println("our sequence has number = " + positionOfSequence);
            out.println("bits for sequence = " + l2);
            out.println("total amount of bits = " + (l1 + l2));

            out.println("encoded string = " + output);
        }
        return result;
    }

    /**
     * Вычисляет и возвращает количество различных композиций
     *
     * @param n длина последовательности
     * @param k объем алфавита
     * @return количество различных композиций
     */
    private BigInteger Ntau(int n, int k) {
        return Utils.binomialCoefficient(n + k - 1, n - 1);
    }

    /**
     * Вычисляет и возвращает вес последовательности (аналог веса Хемминга)
     *
     * @param tau композиция
     * @return вес последовательности
     */
    private int weight(int[] tau) {
        int weight = 0;
        for (int i = 0; i < tau.length; ++i) {
            weight += tau[i];
        }
        return weight;
    }

    /**
     * Вычисляет и возвращает количество последовательностей с фиксированной композицией
     *
     * @param tau композиция
     * @return количество последовательностей с фиксированной композицией
     */
    private BigInteger N(int[] tau) {
        int total = weight(tau);
        BigInteger result = Utils.factorial(total);
        for (int i = 0; i < tau.length; ++i) {
            result = result.divide(Utils.factorial(tau[i]));
        }
        return result;
    }

    /**
     * Вычисляет и возвращает номер позиции композиции
     *
     * @param tau композиция
     * @param n   длина последовательности
     * @return номер позиции композиции
     */
    private BigInteger positionOfComposition(int[] tau, int n) {
        BigInteger J = BigInteger.ZERO;
        int sum = 0;
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < tau[i]; ++j) {
                J = J.add(Ntau(M - i - 1, n - sum - j));
            }
            sum += tau[i];
        }
        return J;
    }

    /**
     * Вычисляет и возвращает номер позиции последовательности
     *
     * @param x последовательность
     * @return номер позиции последовательности
     */
    private BigInteger positionOfSequence(char[] x) {
        int[] tau = tau(x);
        Set<Character> characters = new TreeSet<>();
        for (char c : x) {
            characters.add(c);
        }
        BigInteger J = BigInteger.ZERO;

        for (int i = 0; i < x.length; ++i) {
            for (Character current : characters) {
                if (current == x[i]) {
                    break;
                }
                if (tau[current] != 0) {
                    tau[current]--;
                    J = J.add(N(tau));
                    tau[current]++;
                }
            }
            tau[x[i]]--;
        }

        return J;
    }

    /**
     * Вычисляет и возвращает композицию
     *
     * @param x последовательность
     * @return композиция
     */
    private int[] tau(char[] x) {
        int[] tau = new int[M];
        for (int i = 0; i < x.length; ++i) {
            ++tau[x[i]];
        }
        return tau;
    }

}