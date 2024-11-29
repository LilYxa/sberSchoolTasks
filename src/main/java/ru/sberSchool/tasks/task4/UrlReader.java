package ru.sberSchool.tasks.task4;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task4.exceptions.HttpRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The {@code UrlReader} class provides a method to read the content of a URL.
 * It sends an HTTP GET request to the specified URL and reads the response if the server returns a successful HTTP status code.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class UrlReader {

    /**
     * Reads the content from the specified URL.
     * Sends an HTTP GET request to the provided URL and logs the server's response content.
     *
     * @param urlString the URL to which the GET request will be sent.
     * @return the HTTP response code returned by the server.
     * @throws HttpRequestException if the server returns a non-OK HTTP status code.
     * @throws IOException if an I/O error occurs while reading the content from the URL.
     */
    public static int readContent(String urlString) throws HttpRequestException, IOException {
        log.debug("readContent[0]: Начало чтения содержимого URL: {}", urlString);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        log.debug("readContent[1]: Установлено соединение с URL: {}", urlString);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        log.debug("readContent[2]: Код ответа сервера: {}", responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new HttpRequestException(String.format(Constants.SERVER_CODE_RESPONSE_MESSAGE, responseCode));
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            log.info("Содержимое сайта:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        log.debug("readContent[3]: Завершено чтение содержимого URL: {}", urlString);

        return responseCode;
    }
}
