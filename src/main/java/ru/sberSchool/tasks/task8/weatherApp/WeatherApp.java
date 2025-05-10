package ru.sberSchool.tasks.task8.weatherApp;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherRequest;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherResponse;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.RequestException;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.WeatherApiException;
import ru.sberSchool.tasks.task8.weatherApp.service.WeatherService;
import ru.sberSchool.tasks.task8.weatherApp.service.impl.WeatherServiceImpl;

import java.util.Scanner;

/**
 * Entry point of the Weather application.
 *
 * @author Elland Ilia
 */
@Slf4j
public class WeatherApp {

    private static WeatherService weatherService;

    /**
     * Main method that starts the Weather application.
     *
     * @param args command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        weatherService = new WeatherServiceImpl();

        Scanner scanner = new Scanner(System.in);
        log.info("Введите название города: ");
        String city = scanner.nextLine();
        WeatherRequest weatherRequest = new WeatherRequest(city);

        try {
            WeatherResponse weatherResponse = weatherService.getWeatherData(weatherRequest);
            double temperature = weatherResponse.getCurrent().getTemp_c();
            String condition = weatherResponse.getCurrent().getCondition().getText();

            log.info("Температура: {}", temperature);
            log.info("Облачность: {}", condition);

        } catch (RequestException | WeatherApiException e) {
            log.error("Ошибка в ходе выполнения: {}", e.getMessage());
        }
    }
}
