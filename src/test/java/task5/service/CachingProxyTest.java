package task5.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task5.service.CachingProxy;
import ru.sberSchool.tasks.task5.service.Calculator;
import ru.sberSchool.tasks.task5.service.impl.CalculatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class CachingProxyTest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        CalculatorImpl target = new CalculatorImpl();
        calculator = CachingProxy.createProxy(Calculator.class, target);
    }

    @Test
    public void testCachingFirstCall() {
        log.debug("testCachingFirstCall[0]: start test");
        int result = calculator.calc(5);
        assertEquals(120, result);
    }

    @Test
    public void testCachingSubsequentCall() {
        log.debug("testCachingSubsequentCall[0]: start test");
        int result1 = calculator.calc(5);
        log.debug("testCachingSubsequentCall[1]: result1: {}", result1);
        assertEquals(120, result1);

        int result2 = calculator.calc(5);
        log.debug("testCachingSubsequentCall[2]: result2: {}", result2);
        assertEquals(120, result2);
    }

    @Test
    public void testCachingWithDifferentArguments() {
        log.debug("testCachingWithDifferentArguments[0]: start test");
        int result1 = calculator.calc(5);
        log.debug("testCachingWithDifferentArguments[1]: result1: {}", result1);
        assertEquals(120, result1);

        int result2 = calculator.calc(6);
        log.debug("testCachingWithDifferentArguments[2]: result2: {}", result2);
        assertEquals(720, result2);

        int result3 = calculator.calc(5);
        log.debug("testCachingWithDifferentArguments[3]: result3: {}", result3);
        assertEquals(120, result3);

        int result4 = calculator.calc(6);
        log.debug("testCachingWithDifferentArguments[4]: result4: {}", result4);
        assertEquals(720, result4);
    }

}
