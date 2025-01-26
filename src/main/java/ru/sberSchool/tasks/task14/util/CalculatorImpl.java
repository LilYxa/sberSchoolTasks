package ru.sberSchool.tasks.task14.util;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task14.annotations.Cachable;
import ru.sberSchool.tasks.task14.service.DataSource;
import ru.sberSchool.tasks.task14.service.impl.PostgreSQLDataSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code CalculatorImpl} class is an implementation of the {@link Calculator} interface.
 * It provides the functionality to calculate the Fibonacci sequence and caches the results
 * using a data source specified by the {@link Cachable} annotation.
 *
 * @see Calculator
 * @see Cachable
 * @see DataSource
 *
 * @author Elland Ilia
 */
@Slf4j
public class CalculatorImpl implements Calculator {
    private final ConcurrentHashMap<Method, DataSource> sourceCache = new ConcurrentHashMap<>();

    @Cachable(PostgreSQLDataSource.class)
    public List<Integer> fibonacci(int n) {
        log.debug("fibonacci[0]: Executing Fibonacci calculation for n = {}", n);
        List<Integer> result = new ArrayList<>();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            result.add(a);
            int temp = a;
            a = b;
            b = temp + b;
        }
        log.debug("fibonacci[1]: Fibonacci calculation completed for n = {}", n);
        return result;
    }

}
