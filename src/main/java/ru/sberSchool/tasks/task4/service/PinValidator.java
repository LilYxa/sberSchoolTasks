package ru.sberSchool.tasks.task4.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task4.exceptions.InvalidPinException;
import ru.sberSchool.tasks.task4.exceptions.PinBlockedException;

/**
 * A utility class for validating PIN inputs during terminal operations.
 * Maintains the correct PIN, tracks failed attempts, and enforces a limit on incorrect attempts.
 *
 * @author Elland Ilia
 * @version 1.0
 */
 @Slf4j
@Data
@NoArgsConstructor
public class PinValidator {

    private String pin;

    private StringBuilder currentPin = new StringBuilder();
    private int failedAttempts;


    public PinValidator(String pin) {
        this.pin = pin;
    }

    /**
     * Processes a single digit entered as part of the PIN.
     *
     * <p>If the length of the entered PIN matches the required PIN length,
     * the input is validated against the correct PIN. Upon successful validation,
     * the failed attempts counter is reset. Otherwise, the counter is incremented,
     * and an exception is thrown if the maximum number of failed attempts is exceeded.</p>
     *
     * @param digit the digit entered as part of the PIN
     * @return {@code true} if the PIN is validated successfully; {@code false} if more digits are required
     * @throws PinBlockedException if the number of failed attempts exceeds the limit
     * @throws InvalidPinException if the entered PIN does not match the correct PIN
     */
    public boolean enterPinDigit(char digit) throws PinBlockedException, InvalidPinException {
        log.debug("enterPinDigit[0]: enter pin digit: {}", digit);
        currentPin.append(digit);

        if (currentPin.length() == Constants.PIN_LENGTH) {
            if (currentPin.toString().equals(pin)) {
                failedAttempts = 0;
                currentPin.setLength(0);
                return true;
            } else {
                failedAttempts++;
                currentPin.setLength(0);

                if (failedAttempts >= 3) {
                    throw new PinBlockedException(Constants.THREE_ATTEMPTS_MESSAGE);
                } else {
                    throw new InvalidPinException(Constants.INVALID_PIN_MESSAGE);
                }
            }
        }
        return false;
    }
}
