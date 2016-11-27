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
        String source = "After dinner sit a while, after supper walk a mile";//"abaaaababa";
        Algorithm[] algorithms = {
                new NumericAlgorithm()
        };
        for (Algorithm algorithm : algorithms) {
            algorithm.encode(source, true);
        }
    }
}
