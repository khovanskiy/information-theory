package com.example;

/**
 * victor
 */
public class Matrix<T extends MathEntry> {

    public static int[][] swap(int[][] a, int i, int j) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        int[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        return a;
    }

    public static int[][] sub(int[][] a, int i, int j, int q) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        for (int k = 0; k < a[i].length; ++k) {
            a[i][k] = (q + a[i][k] - a[j][k]) % q;
        }
        return a;
    }

    public static int[][] add(int[][] a, int i, int j, int q) {
        assert i >= 0 && i < a.length;
        assert j >= 0 && j < a.length;
        for (int k = 0; k < a[i].length; ++k) {
            a[i][k] = (a[i][k] + a[j][k]) % q;
        }
        return a;
    }

    public static int[][] transpose(int[][] a) {
        int k = a.length;
        assert a.length > 0;
        int n = a[0].length;
        int[][] b = new int[n][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < n; ++j) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }
}
