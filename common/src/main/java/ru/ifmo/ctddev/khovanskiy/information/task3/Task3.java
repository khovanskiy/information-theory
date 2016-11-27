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
        String source = "IF_WE_CANNOT_DO_AS_WE_WOULD_WE_SHOULD_DO_AS_WE_CAN";
        //String source = "After dinner sit a while, after supper walk a mile";
        //String source = "abaaaababa";
        Algorithm[] algorithms = {
                new HuffmanAlgorithm()
        };
        for (Algorithm algorithm : algorithms) {
            algorithm.encode(source, true);
        }
    }
}
