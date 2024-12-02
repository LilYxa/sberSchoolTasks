package ru.sberSchool.tasks.task5.service.impl;

import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task5.service.Calculator;

/**
 * The {@code CalculatorImpl} class is an implementation of the {@link Calculator} interface.
 * This class provides a concrete implementation of the {@code calc} method that calculates
 * the factorial of a given non-negative integer.
 *
 * @see Calculator
 *
 * @author Elland Ilia
 * @version 1.0
 */
public class CalculatorImpl implements Calculator {

    /**
     * Calculates the factorial of a given non-negative integer.
     *
     * @param number the non-negative integer input for the calculation
     * @return the factorial of the input number
     * @throws IllegalArgumentException if the input number is negative
     */
    @Override
    public int calc(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(Constants.NON_NEGATIVE_NUMBER_MESSAGE);
        }

        if (number == 0 || number == 1) {
            return 1;
        }
        return number * calc(number - 1);
    }

}
