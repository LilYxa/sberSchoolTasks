package ru.sberSchool.tasks.task4.service;

import ru.sberSchool.tasks.task4.exceptions.AccountIsLockedException;
import ru.sberSchool.tasks.task4.exceptions.InsufficientFundsException;
import ru.sberSchool.tasks.task4.exceptions.InvalidAmountException;
import ru.sberSchool.tasks.task4.exceptions.InvalidPinException;

/**
 * Represents a banking terminal interface that provides basic operations
 * for interacting with a user's account. This includes entering a PIN,
 * checking the account balance, depositing money, and withdrawing money.
 *
 * @author Elland Ilia
 * @version 1.0
 */
public interface Terminal {

    /**
     * Enters a single digit of the user's PIN.
     * A complete PIN typically consists of multiple calls to this method.
     *
     * @param digit the digit of the PIN being entered (must be a character representing a digit: '0'-'9').
     * @throws InvalidPinException if the entered digit is invalid or if the complete PIN is incorrect.
     * @throws AccountIsLockedException if the account is currently locked due to multiple failed PIN attempts.
     */
    void enterPin(char digit) throws InvalidPinException, AccountIsLockedException;

    /**
     * Retrieves the current balance of the user's account.
     *
     * @return the balance of the account.
     * @throws AccountIsLockedException if the account is currently locked.
     */
    double checkBalance() throws AccountIsLockedException;

    /**
     * Deposits a specified amount of money into the user's account.
     *
     * @param amount the amount of money to deposit. The amount must be a positive integer and typically
     *               must meet specific rules, such as being a multiple of a certain value.
     * @return the deposited amount.
     * @throws AccountIsLockedException if the account is currently locked.
     * @throws InvalidAmountException if the amount is invalid (e.g., not a multiple of the required value).
     */
    int depositMoney(int amount) throws AccountIsLockedException, InvalidAmountException;

    /**
     * Withdraws a specified amount of money from the user's account.
     *
     * @param amount the amount of money to withdraw. The amount must be a positive integer and typically
     *               must meet specific rules, such as being a multiple of a certain value.
     * @return the withdrawn amount.
     * @throws AccountIsLockedException if the account is currently locked.
     * @throws InvalidAmountException if the amount is invalid (e.g., not a multiple of the required value).
     * @throws InsufficientFundsException if there are insufficient funds in the account for the withdrawal.
     */
    int withdrawMoney(int amount) throws AccountIsLockedException, InvalidAmountException, InsufficientFundsException;
}
