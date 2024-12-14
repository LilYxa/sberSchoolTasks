package ru.sberSchool.tasks.task5.service;

import ru.sberSchool.tasks.task5.annotations.Cache;
import ru.sberSchool.tasks.task5.annotations.Metric;

/**
 * The {@code Calculator} interface defines a contract for a simple calculator
 * that performs a calculation on a single integer input.
 *
 * @author Elland Ilia
 * @version 1.0
 */
public interface Calculator {

    /**
     * Performs a calculation on the given number.
     *
     * @param number the integer input for the calculation
     * @return the result of the calculation, typically an integer
     */
    @Cache
    @Metric
    int calc(int number);
}
