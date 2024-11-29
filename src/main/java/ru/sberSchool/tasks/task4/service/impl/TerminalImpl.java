package ru.sberSchool.tasks.task4.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task4.exceptions.*;
import ru.sberSchool.tasks.task4.service.PinValidator;
import ru.sberSchool.tasks.task4.service.Terminal;
import ru.sberSchool.tasks.task4.service.TerminalServer;

/**
 * Implementation of the {@link Terminal} interface.
 * Provides operations for interacting with a terminal, including PIN validation,
 * checking account balance, depositing money, and withdrawing money.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
@Data
public class TerminalImpl implements Terminal {

    private final TerminalServer terminalServer;

    private final PinValidator pinValidator;

    private long lockEndTime = 0;

    @Override
    public void enterPin(char digit) throws InvalidPinException, AccountIsLockedException {
        log.debug("enterPin[0]: enter digit: {}", digit);
        checkAccountLock();
        if (!Character.isDigit(digit)) {
            log.warn("Неверный символ: {}. Ожидается цифра.", digit);
            throw new InvalidPinException(Constants.ONLY_DIGIT_MESSAGE);
        }

        try {
            if (pinValidator.enterPinDigit(digit)) {
                log.info("Пин-код принят.");
            }
        } catch (InvalidPinException e) {
            log.error(Constants.INVALID_PIN_MESSAGE);
            throw new InvalidPinException(e.getMessage());
        } catch (PinBlockedException e) {
            lockEndTime = System.currentTimeMillis() + Constants.LOCK_TIME_SECONDS * 1000;
            log.error("Аккаунт заблокирован на {} секунд.", Constants.LOCK_TIME_SECONDS);
            throw new AccountIsLockedException("Аккаунт заблокирован на " + Constants.LOCK_TIME_SECONDS + " секунд.");
        }
    }

    @Override
    public double checkBalance() throws AccountIsLockedException {
        log.debug("checkBalance[0]: check balance");
        checkAccountLock();
        double balance = terminalServer.getBalance();
        log.info("Баланс: {}", balance);
        return balance;
    }

    @Override
    public int depositMoney(int amount) throws AccountIsLockedException, InvalidAmountException {
        log.debug("depositMoney[0]: deposit amount: {}", amount);
        checkAccountLock();
        validateAmount(amount);
        terminalServer.deposit(amount);
        log.info("На счет зачислено: {}", amount);
        return amount;
    }

    @Override
    public int withdrawMoney(int amount) throws AccountIsLockedException, InvalidAmountException, InsufficientFundsException {
        log.debug("withdrawMoney[0]: withdraw amount: {}", amount);
        checkAccountLock();
        validateAmount(amount);

        if (!terminalServer.withdraw(amount)) {
            log.error("Недостаточно средств для снятия: {}", amount);
            throw new InsufficientFundsException("Недостаточно средств на счете.");
        }
        log.info("Снято со счета: {}", amount);
        return amount;
    }

    /**
     * Checks if the account is currently locked due to multiple failed PIN attempts.
     * If the account is locked, an exception is thrown with the remaining lock time.
     *
     * @throws AccountIsLockedException if the account is locked, indicating the remaining time in seconds.
     */
    private void checkAccountLock() throws AccountIsLockedException {
        log.debug("checkAccountLock[0]: checking account");
        if (System.currentTimeMillis() < lockEndTime) {
            long remainingTime = (lockEndTime - System.currentTimeMillis()) / 1000;
            log.error("Попытка доступа к заблокированному аккаунту. Оставшееся время блокировки: {} сек.", remainingTime);
            throw new AccountIsLockedException(String.format(Constants.ACCOUNT_LOCKED_MESSAGE, remainingTime));
        }
    }

    /**
     * Validates that the specified amount adheres to the system's requirements.
     * For example, the amount must be a positive integer and divisible by a specific value (e.g., 100).
     *
     * @param amount the amount to validate.
     * @throws InvalidAmountException if the amount is invalid (e.g., not a multiple of the required value).
     */
    private void validateAmount(int amount) throws InvalidAmountException {
        if (amount % Constants.AMOUNT_MULTIPLICITY != 0) {
            log.error("Сумма должна быть кратна 100: {}", amount);
            throw new InvalidAmountException(Constants.INVALID_AMOUNT_MESSAGE);
        }
    }
}
