package ru.sberSchool.tasks.task4;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task4.exceptions.HttpRequestException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import static ru.sberSchool.tasks.task4.UrlReader.readContent;

/**
 * The {@code UrlContentReaderApplication} class provides a command-line application that allows the user
 * to input a URL and read its content. The application attempts to fetch content from the specified URL, handling
 * potential errors such as invalid URLs or I/O issues.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class UrlContentReaderApplication {

    /**
     * The entry point of the application. This method prompts the user to enter a URL and attempts to read
     * the content from the URL. It catches and handles potential exceptions related to invalid URLs and I/O errors.
     *
     * @param args the command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            log.info("Введите URL ресурса: ");
            String url = scanner.nextLine();

            try {
                readContent(url);
                break;
            } catch (MalformedURLException e) {
                log.warn("Введён некорректный URL: {}", url);
                System.out.println("Ошибка: Неверный формат URL. Попробуйте снова.");
            } catch (IOException | HttpRequestException e) {
                log.error("Ошибка при попытке доступа к ресурсу {}: {}", url, e.getMessage());
                System.out.println("Ошибка: Невозможно получить доступ к ресурсу. Попробуйте снова.");
            }
        }
        scanner.close();
    }
}
