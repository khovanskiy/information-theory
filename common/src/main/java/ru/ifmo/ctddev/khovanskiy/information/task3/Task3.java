package ru.ifmo.ctddev.khovanskiy.information.task3;

/**
 * victor
 */
public class Task3 implements Runnable {
    public static void main(String[] args) {
        new Task3().run();
    }

    @Override
    public void run() {
        String source = "cabbacbcbb";
        Algorithm[] algorithms = {
                new AdaptiveArithmeticAlgorithm()
        };
        for (Algorithm algorithm : algorithms) {
            algorithm.encode(source, true);
        }
    }
}
