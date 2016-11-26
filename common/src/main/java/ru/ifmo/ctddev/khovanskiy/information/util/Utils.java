package ru.ifmo.ctddev.khovanskiy.information.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author victor
 */
public class Utils {
    public static final BigDecimal TWO = BigDecimal.valueOf(2);
    /**
     * Возвращает логарифим параметра {@code x} по основанию {@code i}
     *
     * @param arg  параметр
     * @param base основание логарифма
     * @return результат функции
     */
    public static double log(double arg, double base) {
        return Math.log(arg) / Math.log(base);
    }

    /**
     * e.g. [1, 1, 0, 0, 1] -> "11001"
     */
    public static String convertBinListToString(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : list) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    public static BigDecimal integerPart(BigDecimal decimal) {
        return decimal.setScale(0, BigDecimal.ROUND_FLOOR);
    }

    /**
     * Возвращает дробную часть вещественного числа
     *
     * @param decimal
     *
     * @return дробная часть
     */
    public static BigDecimal fractionalPart(BigDecimal decimal) {
        return decimal.remainder(BigDecimal.ONE);
    }

    public static List<Integer> fractionalPartToBinList(BigDecimal result, int l) {
        List<Integer> code = new ArrayList<>();
        for (int i = 0; i < l; ++i) {
            result = result.multiply(TWO);
            if (result.compareTo(BigDecimal.ONE) >= 0) {
                code.add(1);
                result = result.subtract(BigDecimal.ONE);
            } else {
                code.add(0);
            }
        }
        return code;
    }
}
