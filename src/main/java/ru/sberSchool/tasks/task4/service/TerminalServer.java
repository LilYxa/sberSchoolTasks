package ru.sberSchool.tasks.task4.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A class that represents the server handling basic terminal operations.
 * It manages a balance and provides methods for depositing and withdrawing money.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalServer {

    private double balance;

    /**
     * Deposits the specified amount into the balance.
     *
     * @param amount the amount to be deposited. Must be a positive integer.
     */
    public void deposit(int amount) {
        balance += amount;
    }

    /**
     * Attempts to withdraw the specified amount from the balance.
     * If the balance is sufficient, the amount is deducted, and the method returns {@code true}.
     * If the balance is insufficient, the balance remains unchanged, and the method returns {@code false}.
     *
     * @param amount the amount to be withdrawn. Must be a positive integer.
     * @return {@code true} if the withdrawal was successful; {@code false} otherwise.
     */
    public boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
