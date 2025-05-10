package ru.sberSchool.tasks.task14;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task14.service.impl.PostgreSQLDataSource;
import ru.sberSchool.tasks.task14.util.Calculator;
import ru.sberSchool.tasks.task14.util.CalculatorImpl;
import ru.sberSchool.tasks.task14.util.CalculatorProxy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CalculatorTest {

    private Calculator calculatorProxy;
    private PostgreSQLDataSource dataSource;

    @BeforeEach
    public void setUp() throws Exception {
        log.debug("setUp[0]: Setting up test environment.");

        // Настройка прокси
        CalculatorImpl calculatorImpl = new CalculatorImpl();
        calculatorProxy = CalculatorProxy.createProxy(calculatorImpl);

        // Инициализация базы данных
        dataSource = new PostgreSQLDataSource();
        dataSource.init();
    }

    @Test
    public void testFibonacciCaching() {
        log.debug("testFibonacciCaching[0]: Testing Fibonacci caching in database.");

        // Вызов через прокси
        List<Integer> result = calculatorProxy.fibonacci(5);
        log.debug("testFibonacciCaching[1]: Verifying Fibonacci result for n = 5.");
        assertEquals(List.of(0, 1, 1, 2, 3), result, "Fibonacci sequence is incorrect.");
        log.debug("testFibonacciCaching[2]: Result: {}", result);

        // Проверяем, что данные были сохранены в базе
        assertEquals("[0, 1, 1, 2, 3]", dataSource.get("fibonacci(5)"));
    }

    @Test
    public void testCacheReuse() {
        log.debug("testCacheReuse[0]: Testing cache reuse for Fibonacci.");

        // Выполняем первый вызов
        List<Integer> result = calculatorProxy.fibonacci(6);
        log.debug("testCacheReuse[1]: First result: {}.", result);

        // Выполняем второй вызов, который должен использовать кэш
        List<Integer> cachedResult = calculatorProxy.fibonacci(6);
        log.debug("testCacheReuse[2]: Cached result: {}.", cachedResult);

        log.debug("testCacheReuse[3]: Verifying cache reuse for Fibonacci(6).");
        assertEquals(result, cachedResult, "Cached result should match the original result.");
    }
}
