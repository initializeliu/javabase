package com.spring.aop;

import org.springframework.stereotype.Component;

@Component
public class MyCalculator implements Calculator{
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
