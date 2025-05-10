package ru.sberSchool.tasks.task10.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task10.exceptions.EmptyFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FactorialUtilTest {

    private static final String VALID_FILE = "valid_numbers";
    private static final String INVALID_FILE = "invalid_numbers.txt";
    private static final String MISSING_FILE = "missing_file.txt";
    private static final String EMPTY_FILE = "empty_file.txt";

    @BeforeAll
    static void setup() throws IOException {
        log.debug("setup[0]: Creating test files");

        // Создаем файл с валидными числами
        FileGenerationUtil.generateFileWithNumbers(VALID_FILE, 20);

        // Создаем файл с невалидными значениями
        Files.write(Path.of(INVALID_FILE), String.join("\n", "abc", "10", "-5", "invalid").getBytes());

        // Создаем пустой файл
        Files.write(Path.of(EMPTY_FILE), "".getBytes());
    }

    @AfterAll
    static void teardown() throws IOException {
        log.debug("teardown[0]: Cleaning up test files");

        Files.deleteIfExists(Path.of(VALID_FILE + Constants.TXT_EXTENSION));
        Files.deleteIfExists(Path.of(INVALID_FILE));
        Files.deleteIfExists(Path.of(EMPTY_FILE));
    }

    @Test
    @DisplayName("calculateFactorialFromFile: Positive test - valid numbers")
    void testCalculateFactorialFromFile_ValidFile() {
        log.debug("testCalculateFactorialFromFile_ValidFile[0]: Start test");

        Assertions.assertDoesNotThrow(() -> {
            FactorialUtil.calculateFactorialFromFile(VALID_FILE + Constants.TXT_EXTENSION);
        });
        log.debug("testCalculateFactorialFromFile_ValidFile[1]: Test passed");
    }

    @Test
    @DisplayName("calculateFactorialFromFile: Negative test - invalid values in file")
    void testCalculateFactorialFromFile_InvalidValues() {
        log.debug("testCalculateFactorialFromFile_InvalidValues[0]: Start test");

        Assertions.assertThrows(NumberFormatException.class, () -> {
            FactorialUtil.calculateFactorialFromFile(INVALID_FILE);
        });
        log.debug("testCalculateFactorialFromFile_InvalidValues[1]: Test passed");
    }

    @Test
    @DisplayName("calculateFactorialFromFile: Negative test - missing file")
    void testCalculateFactorialFromFile_MissingFile() {
        log.debug("testCalculateFactorialFromFile_MissingFile[0]: Start test");

        Assertions.assertThrows(IOException.class, () -> {
            FactorialUtil.calculateFactorialFromFile(MISSING_FILE);
        });
        log.debug("testCalculateFactorialFromFile_MissingFile[1]: Test passed");
    }

    @Test
    @DisplayName("calculateFactorialFromFile: Edge case - empty file")
    void testCalculateFactorialFromFile_EmptyFile() throws IOException {
        log.debug("testCalculateFactorialFromFile_EmptyFile[0]: Start test");

        Assertions.assertThrows(EmptyFileException.class, () -> {
            FactorialUtil.calculateFactorialFromFile(EMPTY_FILE);
        });

        Files.deleteIfExists(Path.of(EMPTY_FILE));
        log.debug("testCalculateFactorialFromFile_EmptyFile[1]: Test passed");
    }
}

