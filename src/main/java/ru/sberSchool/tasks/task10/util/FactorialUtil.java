package ru.sberSchool.tasks.task10.util;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task10.exceptions.EmptyFileException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for calculating the factorial of numbers from a file.
 * This class reads numbers from a specified file, calculates their factorials in separate threads,
 * and logs the results. The factorial computation is performed using BigInteger to handle large numbers.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class FactorialUtil {

    /**
     * Reads numbers from a file, calculates their factorials in separate threads,
     * and logs the results. If the file is empty or contains invalid data, an exception is thrown.
     *
     * @param fileName The name of the file containing the numbers for which factorials will be calculated.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws EmptyFileException If the file is empty.
     */
    public static void calculateFactorialFromFile(String fileName) throws IOException, EmptyFileException {
        log.debug("calculateFactorialFromFile[0]: File name: {}", fileName);
        List<Thread> threads = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));

            // Check if the file is empty
            if (lines.isEmpty()) {
                log.error("calculateFactorialFromFile[0]: File is empty!");
                throw new EmptyFileException(Constants.EMPTY_FILE_MESSAGE);
            }

            // Process each line to calculate factorial
            lines.forEach(line -> {
                try {
                    int number = Integer.parseInt(line.trim());

                    // Create a new thread to calculate the factorial
                    Thread thread = new Thread(() -> {
                        BigInteger factorial = factorial(number);
                        log.info("calculateFactorialFromFile[0]: Факториал числа {} равен {}", number, factorial);
                    });
                    thread.start();
                    threads.add(thread);
                } catch (NumberFormatException e) {
                    log.error("calculateFactorialFromFile[1]: Incorrect value: {}", line);
                    throw new NumberFormatException(e.getMessage());
                }
            });

            // Wait for all threads to finish
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    log.error("calculateFactorialFromFile[2]: Thread interrupted: {}", e.getMessage());
                }
            });

        } catch (IOException e) {
            log.error("calculateFactorialFromFile[3]: Error: {}", e.getMessage());
            throw new IOException(e);
        }
    }

    /**
     * Calculates the factorial of a given number.
     *
     * @param n The number for which the factorial is calculated.
     * @return The factorial of the given number as a BigInteger.
     */
    private static BigInteger factorial(int n) {
        log.debug("factorial[0]: Calculating factorial of {}", n);
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }
}
