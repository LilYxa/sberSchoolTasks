package ru.sberSchool.tasks.task8.weatherApp.service;

import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherRequest;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherResponse;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.RequestException;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.WeatherApiException;

/**
 * Service interface for fetching weather data.
 *
 * @author Elland Ilia
 */
public interface WeatherService {

    /**
     * Retrieves weather data for a specified city.
     *
     * @param weatherRequest the request containing the city name
     * @return the weather response containing temperature and condition details
     * @throws RequestException if there is an error during the API request
     * @throws WeatherApiException if the API response is invalid or cannot be processed
     */
    public WeatherResponse getWeatherData(WeatherRequest weatherRequest) throws RequestException, WeatherApiException;
}
