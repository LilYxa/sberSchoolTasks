package ru.sberSchool.tasks.task4;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task4.exceptions.*;
import ru.sberSchool.tasks.task4.service.PinValidator;
import ru.sberSchool.tasks.task4.service.TerminalServer;
import ru.sberSchool.tasks.task4.service.impl.TerminalImpl;

import java.util.Scanner;

/**
 * A console-based application that simulates a terminal for managing a user's account.
 *
 * @author Elland Ilia
 * @version 1.0
 */
 @Slf4j
public class TerminalApplication {

    /**
     * The entry point of the terminal application.
     *
     * <p>The main method initializes the terminal system with:
     * <ul>
     *   <li>A {@link TerminalServer} instance preloaded with funds.</li>
     *   <li>A {@link PinValidator} initialized with a default PIN.</li>
     *   <li>A {@link TerminalImpl} instance that serves as the core of the terminal's operations.</li>
     * </ul>
     * </p>
     *
     * <p>The method continuously interacts with the user via the console to perform operations
     * like authentication, checking the balance, depositing money, withdrawing money, and exiting.</p>
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        TerminalServer terminalServer = new TerminalServer(100000);
        PinValidator pinValidator = new PinValidator("1234");
        TerminalImpl terminal = new TerminalImpl(terminalServer, pinValidator);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean authenticated = false;

            while (!authenticated) {
                try {
                    System.out.print("Введите PIN-код по одной цифре (4 цифры): ");
                    for (int i = 0; i < 4; i++) {
                        char digit = scanner.next().charAt(0);
                        terminal.enterPin(digit);
                    }
                    authenticated = true;
                } catch (InvalidPinException | AccountIsLockedException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            boolean running = true;

            while (running) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Проверить баланс");
                System.out.println("2. Пополнить счет");
                System.out.println("3. Снять деньги");
                System.out.println("4. Выход");
                System.out.print("Введите номер действия: ");

                int choice = scanner.nextInt();

                try {
                    switch (choice) {
                        case 1:
                            System.out.println("Баланс: " + terminal.checkBalance());
                            break;
                        case 2:
                            System.out.print("Введите сумму для пополнения: ");
                            int depositAmount = scanner.nextInt();
                            terminal.depositMoney(depositAmount);
                            System.out.println("Счет пополнен на: " + depositAmount);
                            break;
                        case 3:
                            System.out.print("Введите сумму для снятия: ");
                            int withdrawAmount = scanner.nextInt();
                            terminal.withdrawMoney(withdrawAmount);
                            System.out.println("Снято: " + withdrawAmount);
                            break;
                        case 4:
                            System.out.println("Завершение работы.");
                            running = false;
                            break;
                        default:
                            System.out.println("Некорректный выбор. Пожалуйста, выберите заново.");
                    }
                } catch (AccountIsLockedException | InvalidAmountException | InsufficientFundsException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }
}
