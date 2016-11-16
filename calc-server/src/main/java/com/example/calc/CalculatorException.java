package com.example.calc;

public class CalculatorException extends RuntimeException {

    CalculatorException(final String s) {
        super(s);
    }

    CalculatorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
