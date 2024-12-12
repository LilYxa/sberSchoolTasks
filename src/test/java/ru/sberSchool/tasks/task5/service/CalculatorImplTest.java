package ru.sberSchool.tasks.task5.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task5.service.Calculator;
import ru.sberSchool.tasks.task5.service.impl.CalculatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class CalculatorImplTest {

    private final Calculator calculator = new CalculatorImpl();

    @Test
    public void testCalc_PositiveNumber() {
        log.debug("testCalc_PositiveNumber[0]: start test");
        int input = 5;
        int expected = 120;

        log.debug("testCalc_PositiveNumber[1]: Testing factorial calculation for input: {}", input);
        int result = calculator.calc(input);
        log.debug("testCalc_PositiveNumber[2]: Result for input {}: {}", input, result);

        assertEquals(expected, result, "Factorial calculation failed for a positive number.");
    }

    @Test
    public void testCalc_Zero() {
        log.debug("testCalc_Zero[0]: start test");
        int input = 0;
        int expected = 1;

        log.debug("testCalc_Zero[1]: Testing factorial calculation for input: {}", input);
        int result = calculator.calc(input);
        log.debug("testCalc_Zero[2]: Result for input {}: {}", input, result);

        assertEquals(expected, result, "Factorial calculation failed for zero.");
    }

    @Test
    public void testCalc_NegativeNumber() {
        log.debug("testCalc_NegativeNumber[0]: start test");
        int input = -1;

        log.debug("testCalc_NegativeNumber[1]: Testing factorial calculation for invalid input: {}", input);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> calculator.calc(input));
        log.debug("testCalc_NegativeNumber[2]: Exception thrown for input {}: {}", input, exception.getMessage());

        assertEquals(Constants.NON_NEGATIVE_NUMBER_MESSAGE, exception.getMessage(),
                "Exception message does not match expected for negative input.");
    }
}
