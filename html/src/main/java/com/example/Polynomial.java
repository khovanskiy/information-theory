package com.example;

import java.util.Arrays;

/**
 * victor
 */
public class Polynomial {
    private static final Polynomial ZERO = Polynomial.of(0);
    private static final Polynomial ONE = Polynomial.of(1);
    private static final Polynomial X = Polynomial.of(0, 1);
    private static final String DEFAULT_VAR = "x";
    private String var = DEFAULT_VAR;

    public Polynomial(String var, int... numbers) {
        this(numbers);
        this.var = var;
    }

    public static Polynomial zero() {
        return ZERO;
    }

    public static Polynomial one() {
        return ONE;
    }

    public static Polynomial x() {
        return X;
    }

    private final int[] numbers;

    private Polynomial(int... numbers) {
        this.numbers = numbers;
    }

    public static Polynomial of(int... numbers) {
        return new Polynomial(numbers);
    }

    public static Polynomial of(String var, int... numbers) {
        return new Polynomial(var, numbers);
    }

    public int length() {
        return numbers.length;
    }

    public int get(int i) {
        return numbers[i];
    }

    public static Polynomial fill(int... shifts) {
        int max = Arrays.stream(shifts).max().getAsInt();
        int[] values = new int[max + 1];
        for (int i = 0; i < shifts.length; ++i) {
            values[shifts[i]] = 1;
        }
        return Polynomial.of(values);
    }

    public static boolean isPrimitive(Polynomial a, int q, int n) {
        Polynomial current = Polynomial.one();
        Polynomial x = Polynomial.x();
        for (int i = 0; i < n; ++i) {
            //System.out.println(i + " | " + current);
            current = current.mul(x, q).mod(a, q);
        }
        return current.equals(Polynomial.one());
    }

    public static Polynomial kxm(int k, int m) {
        assert k != 0;
        int[] numbers = new int[m + 1];
        numbers[m] = k;
        numbers = normalize(numbers);
        return Polynomial.of(numbers);
    }

    public static Polynomial add(Polynomial a, Polynomial b, int q) {
        int sc = Math.max(a.numbers.length, b.numbers.length);
        int[] values = new int[sc];
        for (int i = 0; i < sc; ++i) {
            int cv = 0;
            if (i < a.numbers.length) {
                cv += a.numbers[i];
            }
            if (i < b.numbers.length) {
                cv += b.numbers[i];
            }
            values[i] = cv % q;
        }
        values = normalize(values);
        return Polynomial.of(values);
    }

    public Polynomial add(Polynomial other, int q) {
        return Polynomial.add(this, other, q);
    }

    public static int[] normalize(int[] a) {
        int c = a.length - 1;
        while (c > 0) {
            if (a[c] != 0) {
                break;
            }
            --c;
        }
        if (c == a.length - 1) {
            return a;
        }
        int[] temp = new int[c + 1];
        System.arraycopy(a, 0, temp, 0, temp.length);
        return temp;
    }

    public Polynomial mul(Polynomial other, int q) {
        return Polynomial.mul(this, other, q);
    }

    public static Polynomial mul(Polynomial a, Polynomial b, int q) {
        //System.out.println("(" + a + ")*(" + b + ")");
        Polynomial temp = null;
        for (int i = 0; i < b.numbers.length; ++i) {
            if (b.numbers[i] != 0) {
                Polynomial current = Polynomial.shiftAndMul(a, i, b.numbers[i], q);
                //System.out.println(i + " = " + current);
                if (temp == null) {
                    temp = current;
                } else {
                    temp = Polynomial.add(temp, current, q);
                }
            }
        }
        return temp;
    }

    public Polynomial mod(Polynomial other, int q) {
        return Polynomial.mod(this, other, q);
    }

    public static Polynomial mod(Polynomial a, Polynomial b, int q) {
        Polynomial dividend = a;
        while (dividend.numbers.length >= b.numbers.length) {
            int shift = dividend.numbers.length - b.numbers.length;
            Polynomial temp;
            if (shift > 0) {
                temp = Polynomial.shiftAndMul(b, shift, 1, q);
            } else {
                temp = b;
            }
            dividend = Polynomial.add(dividend, temp, 2);
        }
        return dividend;
    }

    public static Polynomial shiftAndMul(Polynomial p, int shift, int k, int q) {
        int[] values = new int[p.numbers.length + shift];
        System.arraycopy(p.numbers, 0, values, 0, p.numbers.length);
        for (int i = values.length - shift - 1; i >= 0; --i) {
            if (values[i] != 0) {
                int temp = values[i];
                values[i] = 0;
                values[i + shift] = temp * k % q;
            }
        }
        return Polynomial.of(values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(numbers);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Polynomial) {
            Polynomial other = (Polynomial) obj;
            return Arrays.equals(this.numbers, other.numbers);
        }
        return false;
    }

    public String toBitString(int m) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numbers.length; ++i) {
            builder.append(numbers[i]);
        }
        for (int i = 0; i < m - numbers.length; ++i) {
            builder.append("0");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        if (numbers.length == 1 && numbers[0] == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < numbers.length; ++i) {
            if (numbers[i] != 0) {
                if (i == 0) {
                    sb.append(numbers[i]);
                    flag = true;
                } else {
                    if (flag && numbers[i] > 0) {
                        sb.append("+");
                    }
                    if (numbers[i] != 1) {
                        if (numbers[i] == -1) {
                            sb.append("-");
                        } else {
                            sb.append(numbers[i]);
                        }
                    }
                    if (i != 1) {
                        sb.append(var + "<sup>").append(i).append("</sup>");
                    } else {
                        sb.append(var);
                    }
                    flag = true;
                }
            }
        }
        return sb.toString();
    }
}
