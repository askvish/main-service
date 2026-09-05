package com.google.main.util;

public class Calculator {

    private Calculator() {

    }

    public int add(int num1, int num2) {
        return num1 + num2;
    }

    public static int subtract(int num1, int num2) {
        return num1 - num2;
    }

    public static int multiply(int num1, int num2) {
        return num1 * num2;
    }

    public static double divide(int a, int b) {
        return (double) a / b;
    }
}
