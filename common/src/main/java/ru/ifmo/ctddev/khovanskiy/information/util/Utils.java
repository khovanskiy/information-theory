package ru.ifmo.ctddev.khovanskiy.information.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
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
    public static String convertIntListToString(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : list) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    /**
     * e.g. "11001" -> [1, 1, 0, 0, 1]
     */
    public static List<Integer> convertStringToIntList(String input) {
        List<Integer> result = new ArrayList<>();
        for (char c : input.toCharArray()) {
            result.add(Integer.parseInt("" + c));
        }
        return result;
    }

    /**
     * e.g. 5 -> [1, 0, 1]
     */
    public static List<Integer> convertIntToBinaryList(int value) {
        return convertIntToBinaryList(value, value);
    }

    /**
     * e.g. 5 -> [1, 0, 1] + bound (fill remaining positions with zeros)
     */
    public static List<Integer> convertIntToBinaryList(int value, int bound) {
        List<Integer> result = new ArrayList<Integer>();
        while (value != 0) {
            result.add(value % 2);
            value /= 2;
        }
        int cntToAdd = getBitsCount(bound) - result.size();
        for (int i = 0; i < cntToAdd; i++) {
            result.add(0);
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * e.g. 5 -> 3
     */
    public static int getBitsCount(int value) {
        int cnt = 0;
        while (value != 0) {
            cnt++;
            value /= 2;
        }
        return cnt;
    }

    public static List<Integer> charTo01List(int value) {
        List<Integer> res = new ArrayList<>();
        for (int bit = 7; bit >= 0; bit--) {
            res.add((((1 << bit) & value) != 0) ? 1 : 0);
        }
        return res;
    }

    /**
     * e.g. 5 -> "101"
     */
    public static String convertIntToBinaryString(int value, int bound) {
        return convertIntListToString(convertIntToBinaryList(value, bound));
    }

    public static BigDecimal integerPart(BigDecimal decimal) {
        return decimal.setScale(0, BigDecimal.ROUND_FLOOR);
    }

    /**
     * Возвращает дробную часть вещественного числа
     *
     * @param decimal
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

    public static String convertToAscii(String input) {
        try {
            return new String(input.getBytes("ascii"), "ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Integer> getUnaryCode(int value) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < value - 1; i++) {
            result.add(1);
        }
        result.add(0);
        return result;
    }

    public static List<Integer> getMonotoneCode(int value) {
        List<Integer> result = new ArrayList<>();
        result.addAll(getUnaryCode(getBitsCount(value)));
        List<Integer> valueBits = convertIntToBinaryList(value);
        valueBits.remove(0);
        result.addAll(valueBits);
        return result;
    }

    /**
     * Вычисляет и возвращает значение биномиального коэффициента
     *
     * @return значение биномиального коэффициента
     */
    public static BigInteger binomialCoefficient(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k)));
    }

    /**
     * Вычисляет и возвращает значение факториала
     *
     * @return значение факториала
     */
    public static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
