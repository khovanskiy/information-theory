package ru.ifmo.ctddev.khovanskiy.information.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Task1A {

    public static Map<Long, Integer> getSequenceFrequencies(String filePath, int n) throws IOException {
        Map<Long, Integer> result = new HashMap<>();
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        long l;
        byte[] b;
        for (int i = 0; i <= data.length - n; i++) {
            b = Arrays.copyOfRange(data, i, i + n);
            l = 0;
            for (int j = 0; j < n; j++) {
                l = (l << 8) + b[j];
            }
            result.put(l, result.getOrDefault(l, 0) + 1);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        for (int n : new int[]{1,2,4,8}) {
            Map<Long, Integer> frequencies = getSequenceFrequencies("./data/asyoulik.txt", n);
            double enthropy = 0;
            int totalCount = frequencies.entrySet().stream().map(Map.Entry::getValue).reduce(0, (a, b) -> a + b);
            for (Long sequence : frequencies.keySet()) {
                double probability = frequencies.get(sequence) * 1.0 / totalCount;
                enthropy -= probability * Math.log(probability) / Math.log(2);
            }
            System.out.println("Enthropy for n = " + n + " is " + enthropy / n + ", expected size is " + (totalCount + n - 1) * enthropy / n / 8 + " bytes");
        }
    }
}
