package ru.sberSchool.tasks.task10;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for printing a sequence of numbers and letters alternately using two separate threads.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class NumberLetterPrinter {

    /**
     * A volatile flag to control which thread should print next.
     * If true, the number thread will print; if false, the letter thread will print.
     */
    private static volatile boolean printNumbers = true;

    /**
     * Prints numbers and letters alternately in the format:
     * {@code 0 a 1 b 2 c 3 d 4 e 5 f 6 g 7 h 8 i 9 j}.
     * This is achieved using two threads where one prints numbers and the other prints letters.
     * The method waits for one thread to complete its task before switching to the other.
     *
     * @throws InterruptedException If the threads are interrupted during execution.
     */
    public static void printString() throws InterruptedException {
        log.debug("printString[0]: Printing string with numbers and letters");

        // Поток для вывода цифр
        Thread numberThread = new Thread(() -> {
           for (int i = 0; i < 10; i++) {
               while (!printNumbers) {
                   // Подождем, пока не нужно печатать числа
               }
               System.out.print(i + " ");
               // Следующий поток будет выводить букву
               printNumbers = false;
           }
        });

        // Поток для вывода букв
        Thread letterThread = new Thread(() -> {
           char letter = 'a';
           for (int i = 0; i < 10; i++) {
               while (printNumbers) {
                   // Подождем, пока не нужно печатать букву
               }
               System.out.print(letter + " ");
               // Переходим к следующей букве
               letter++;
               // Следующий поток будет выводить цифру
               printNumbers = true;
           }
        });

        // Запускаем оба потока
        numberThread.start();
        letterThread.start();

        // Ожидаем завершения обоих потоков
        numberThread.join();
        letterThread.join();
    }

}
