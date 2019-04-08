package com.example;

import java.util.List;

/**
 * victor
 */
public class Code {
    Group group;
    List<Integer> polynomial;
    int size;
    int distance;
    int left;

    public Code(Group group, List<Integer> polynomial, int size, int distance, int b) {
        this.group = group;
        this.polynomial = polynomial;
        this.size = size;
        this.distance = distance;
        this.left = b;
    }
}