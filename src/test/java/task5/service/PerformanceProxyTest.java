package task5.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task5.service.Calculator;
import ru.sberSchool.tasks.task5.service.PerformanceProxy;
import ru.sberSchool.tasks.task5.service.impl.CalculatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class PerformanceProxyTest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        CalculatorImpl target = new CalculatorImpl();
        calculator = PerformanceProxy.createProxy(Calculator.class, target);
    }

    @Test
    public void testPerformanceFirstCall() {
        log.debug("testPerformanceFirstCall[0]: start test");

        int result = calculator.calc(5);
        log.debug("testPerformanceFirstCall[1]: result: {}", result);

        assertEquals(120, result);
    }

    @Test
    public void testPerformanceSubsequentCall() {
        log.debug("testPerformanceSubsequentCall[0]: start test");

        int result1 = calculator.calc(5);
        log.debug("testPerformanceSubsequentCall[1]: result1: {}", result1);

        int result2 = calculator.calc(5);
        log.debug("testPerformanceSubsequentCall[2]: result2: {}", result2);

        assertEquals(result1, result2);
    }

    @Test
    public void testPerformanceWithDifferentArguments() {
        log.debug("testPerformanceWithDifferentArguments[0]: start test");

        int result1 = calculator.calc(3);
        log.debug("testPerformanceWithDifferentArguments[1]: result1: {}", result1);

        int result2 = calculator.calc(4);
        log.debug("testPerformanceWithDifferentArguments[2]: result2: {}", result2);

        assertEquals(6, result1);
        assertEquals(24, result2);
    }
}
