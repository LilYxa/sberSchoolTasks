package ru.sberSchool.tasks.task10.util;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for generating files containing random numbers.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class FileGenerationUtil {

    /**
     * Generates a file containing random numbers within a specified range.
     * The numbers are written to a file with the specified name, and each number is written
     * on a new line. The file will have the `.txt` extension appended to the file name.
     *
     * @param fileName The name of the file to generate (without extension).
     * @param countNumbers The number of random numbers to generate and write to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void generateFileWithNumbers(String fileName, int countNumbers) throws IOException {
        log.debug("generateFileWithNumbers[0]: Generating file with name: {}", fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + Constants.TXT_EXTENSION))) {
            for (int i = 0; i < countNumbers; i++) {
                writer.write(getRandomNumberFromRange(1, 50) + "\n");
            }
        } catch (IOException e) {
            log.error("generateFileWithNumbers[0]: Error: {}", e.getMessage());
            throw new IOException(e);
        }
    }

    /**
     * Generates a random number within a specified range (inclusive).
     *
     * @param min The minimum value of the range (inclusive).
     * @param max The maximum value of the range (exclusive).
     * @return A random integer within the specified range.
     */
    private static int getRandomNumberFromRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
