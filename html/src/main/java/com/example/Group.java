package com.example;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * victor
 */
public class Group extends ArrayList<Integer> {
    public Group(Integer... numbers) {
        super(Arrays.asList(numbers));
    }
}
