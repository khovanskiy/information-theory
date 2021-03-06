package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Victor Khovanskiy
 */
@Slf4j
public class AdaptiveArithmeticAlgorithm extends Algorithm {

    @Override
    public AdaptiveArithmeticResult encode(String source1, String source2) {
        AdaptiveArithmeticResult result = new AdaptiveArithmeticResult();
        String input = Utils.convertToAscii(source1);
        result.setInput(source2);

        char[] xs = source2.toCharArray();
        char[] x = input.toCharArray();
        int n = input.length();

        // Композиция
        int[] tau = new int[M];
        for (int i = 0; i < x.length; ++i) {
            ++tau[x[i]];
        }

        double[] p = new double[M];

        BigDecimal[] q = new BigDecimal[M];
        q[0] = BigDecimal.ZERO;
        for (int i = 1; i < M; ++i) {
            p[i - 1] = ((double) tau[i - 1]) / n;
            q[i] = q[i - 1].add(BigDecimal.valueOf(p[i - 1]));
        }
        BigDecimal F = BigDecimal.ZERO;
        BigDecimal G = BigDecimal.ONE;

        // Кодирование
        List<AdaptiveArithmeticStepResult> stepResults = new ArrayList<>();
        for (int i = 0; i < x.length; ++i) {
            F = F.add(q[x[i]].multiply(G));
            G = G.multiply(BigDecimal.valueOf(p[x[i]]));

            // Модифицируем композию
            --tau[x[i]];
            // Уменьшаем длину
            --n;
            // Пересчитываем оценки вероятностей и кумулятивных вероятностей
            if (n > 0) {
                for (int j = 1; j < M; j++) {
                    p[j - 1] = ((double) tau[j - 1]) / n;
                    q[j] = q[j - 1].add(BigDecimal.valueOf(p[j - 1]));
                }
            }

            // Запись результата шага
            AdaptiveArithmeticStepResult stepResult = new AdaptiveArithmeticStepResult();
            stepResult.setOrdinal(i);
            stepResult.setX(xs[i]);
            stepResult.setP(p[x[i]]);
            stepResult.setQ(q[x[i]]);
            stepResult.setF(F);
            stepResult.setG(G);
            stepResults.add(stepResult);
        }
        result.setStepResults(stepResults);
        // Формируем кодовое слово
        // Получаем дробную часть
        F = Utils.fractionalPart(F);
        // Количество разрядов после запятой равны длине кодового слова
        int l = (int) Math.ceil(-Utils.log(G.doubleValue(), 2));
        result.setBits(l);
        // Кодовое слово
        List<Integer> code = Utils.fractionalPartToBinList(F, l);
        String output = Utils.convertIntListToString(code);
        result.setOutput(output);

        return result;
    }
}