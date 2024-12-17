package ru.sberSchool.tasks.task10;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class NumberLetterPrinterTest {

    @Test
    public void printStringWorkTest() throws InterruptedException {
        log.debug("printStringWorkTest[0]: Start test");
        NumberLetterPrinter.printString();
        System.out.println();
        log.debug("printStringWorkTest[1]: Test completed");
    }

    @Test
    public void printStringTest() throws InterruptedException {
        log.debug("testPrintString[0]: Start test");
        // Перехватим вывод
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Запускаем метод, который мы тестируем
        NumberLetterPrinter.printString();
        // Проверим результат вывода
        String output = outputStream.toString().trim();

        // Ожидаемый вывод
        String expectedOutput = "0 a 1 b 2 c 3 d 4 e 5 f 6 g 7 h 8 i 9 j";
        // Проверка
        assertEquals(expectedOutput, output, "Output doesn't match the expected result.");
        log.debug("testPrintString[1]: Test completed successfully");
    }
}
