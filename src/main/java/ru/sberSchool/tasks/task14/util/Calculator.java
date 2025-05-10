package ru.sberSchool.tasks.task14.util;

import java.util.List;

/**
 * The {@code Calculator} interface provides a method for calculating Fibonacci numbers.
 *
 * @author Elland Ilia
 */
public interface Calculator {

    /**
     * Calculates the Fibonacci sequence up to the {@code n}-th term.
     * The sequence starts with 0 and 1, and each subsequent number is the sum of the previous two.
     *
     * @param n the number of terms in the Fibonacci sequence to calculate
     * @return a {@code List} of integers representing the Fibonacci sequence up to the {@code n}-th term
     * @throws IllegalArgumentException if {@code n} is less than 1
     */
    List<Integer> fibonacci(int n);
}
