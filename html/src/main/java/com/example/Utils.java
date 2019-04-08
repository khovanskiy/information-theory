package com.example;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * victor
 */
public class Utils {
    public static int C(int n, int k) {
        double res = 1;
        for (int i = 1; i <= k; ++i) {
            res = res * (n - k + i) / i;
        }
        return (int) Math.round(res);
    }

    private static int[] inc(int[] v) {
        int c = 1;
        int[] b = new int[v.length];
        for (int i = v.length - 1; i >= 0; --i) {
            int t = (v[i] + c) % 2;
            int q = (v[i] + c) / 2;
            b[i] = t;
            c = q;
        }
        return b;
    }

    public static int[][] words(int r) {
        int[][] a = new int[1 << r][r];
        for (int i = 1; i < (1 << r); ++i) {
            a[i] = inc(a[i - 1]);
        }
        return a;
    }

    public static String concat(List<?> arrs, String delimeter) {
        if (arrs.size() == 0) {
            return "";
        }
        String temp = "";
        for (int i = 0; i < arrs.size() - 1; ++i) {
            temp += arrs.get(i) + delimeter;
        }
        temp += arrs.get(arrs.size() - 1);
        return temp;
    }

    /**
     * Максимальная длина последовательности идущих подряд элементов
     */
    public static Pair<Integer, Integer> seqLength(Group group) {
        int c = 1;
        int m = 1;
        int b = 0;
        int mb = 0;
        for (int i = 1; i < group.size(); ++i) {
            if (group.get(i - 1) + 1 == group.get(i)) {
                ++c;
                if (c > m) {
                    m = c;
                    mb = b;
                }
            } else {
                c = 1;
                b = i;
            }
        }
        return new Pair<>(m, mb);
    }

    public static Polynomial[][] transpose(Polynomial[][] a) {
        int k = a.length;
        assert a.length > 0;
        int n = a[0].length;
        Polynomial[][] b = new Polynomial[n][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < n; ++j) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    public static boolean isPrime(int a) {
        for (int i = 2; i < a; ++i) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> primes(int a) {
        List<Integer> array = new ArrayList<>();
        for (int i = 2; i < a; ++i) {
            if (a % i == 0 && isPrime(i)) {
                array.add(i);
            }
        }
        return array;
    }

    public static void print(int[][] a) {
        for (int i = 0; i < a.length; ++i) {
            System.out.println(Arrays.toString(a[i]));
        }
    }

    public static int minDistance(int[][] a) {
        int min = Integer.MAX_VALUE;
        int k = a.length;
        int[][] m = words(k);
        for (int i = 1; i < m.length; ++i) {
            int[] c = mul(m[i], a);
            System.out.println(Arrays.toString(m[i]) + " | " + Arrays.toString(c));
            int d = w(c);
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    public static int w(int[] a) {
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] > 0) {
                ++count;
            }
        }
        return count;
    }

    public static int w(List<Integer> a) {
        int count = 0;
        for (int i = 0; i < a.size(); ++i) {
            if (a.get(i) > 0) {
                ++count;
            }
        }
        return count;
    }

    public static List<Integer> random(int n, int count) {
        assert count >= 0;
        List<Integer> temp = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            temp.add(i);
        }
        Collections.shuffle(temp);
        return temp.subList(0, count);
    }

    public static int[] mul(int[] v, int[][] a) {
        assert v.length == a.length;
        int[] b = new int[a[0].length];
        for (int i = 0; i < a[0].length; ++i) {
            for (int j = 0; j < v.length; ++j) {
                b[i] += v[j] * a[j][i];
            }
            b[i] %= 2;
        }
        return b;
    }

    public static Polynomial[] mul(int[] v, Polynomial[][] a, int q) {
        assert v.length == a.length;
        Polynomial[] b = new Polynomial[a[0].length];
        for (int i = 0; i < a[0].length; ++i) {
            b[i] = Polynomial.zero();
            for (int j = 0; j < v.length; ++j) {
                if (v[j] != 0) {
                    b[i] = b[i].add(a[j][i], q);
                }
            }
        }
        return b;
    }

    public static Map<List<Integer>, List<Integer>> syndroms(int[][] h) {
        //System.out.println("H = " + h.length + "x" + h[0].length);
        h = Matrix.transpose(h);
        //print(h);
        //System.out.println("H^T = " + h.length + "x" + h[0].length);
        int r = h.length;
        int n = h[0].length;
        int[][] errs = words(r);
        Map<List<Integer>, List<Integer>> cache = new HashMap<>();
        //int[][] b = new int[errs.length][h[0].length];
        for (int i = 0; i < errs.length; ++i) {
            List<Integer> value = Arrays.stream(errs[i]).boxed().collect(Collectors.toList());
            List<Integer> key = Arrays.stream(mul(errs[i], h)).boxed().collect(Collectors.toList());
            List<Integer> oldValue = cache.get(key);
            if (oldValue == null) {
                cache.put(key, value);
            } else if (w(oldValue) > w(value)) {
                cache.put(key, value);
            }
        }
        return cache;
    }

    /*public static Map<Polynomial[], int[]> syndroms(Polynomial[][] h, int q) {
        h = transpose(h);
        int r = h.length;
        int n = h[0].length;
        int[][] errs = words(r);
        Map<Polynomial[], int[]> b = new HashMap<>();
        Set<String> cache = new HashSet<>();
        //int[][] b = new int[errs.length][h[0].length];
        for (int i = 0; i < errs.length; ++i) {
            int[] val = errs[i];
            Polynomial[] key = mul(val, h, q);
            if (!cache.contains(Arrays.toString(key))) {
                b.put(key, val);
                cache.add(Arrays.toString(key));
            }
        }
        return b;
    }*/

    public static int b(int[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i] == 1) {
                return i;
            }
        }
        return 0;
    }

    public static int e(int[] x) {
        for (int i = x.length - 1; i >= 0; i--) {
            if (x[i] == 1) {
                return i;
            }
        }
        return 0;
    }

    public static int span(int[] x) {
        return e(x) - b(x);
    }

    public static boolean checkMCF(int[][] x) {
        Set<Integer> bb = new HashSet<>();
        for (int i = 0; i < x.length; ++i) {
            int cb = b(x[i]);
            if (bb.contains(cb)) {
                return false;
            }
            bb.add(cb);
        }
        return true;
    }

    public static boolean checkIP(int[][] a) {
        assert a.length > 0;
        int k = a.length;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (i == j && a[i][j] != 1 || i != j && a[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkPI(int[][] a) {
        assert a.length > 0;
        int k = a.length;
        int n = a[0].length;
        for (int i = 0; i < k; ++i) {
            for (int j = n - k; j < n; ++j) {
                if (i == (j - n + k) && a[i][j] != 1 || i != (j - n + k) && a[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[][] convert(int[][] a) {
        if (checkIP(a)) {
            int k = a.length;
            int n = a[0].length;
            assert k <= n;
            int r = n - k;
            int[][] b = new int[r][n];
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < r; ++j) {
                    b[j][i] = a[i][j + k];
                }
            }
            for (int i = 0; i < r; ++i) {
                b[i][i + k] = 1;
            }
            return b;
        } else if (checkPI(a)) {
            int r = a.length;
            int n = a[0].length;
            int k = n - r;
            int[][] b = new int[k][n];
            for (int i = 0; i < r; ++i) {
                for (int j = 0; j < k; ++j) {
                    b[j][i + k] = a[i][j];
                }
            }
            for (int i = 0; i < k; ++i) {
                b[i][i] = 1;
            }
            return b;
        }
        throw new IllegalStateException("Матрица находится не в нужной форме.");
    }
}
