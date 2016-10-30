/**
 * @author victor
 */
public class Utils {
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
}
