package ru.sberSchool.tasks.task8.weatherApp.service.impl;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherRequest;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherResponse;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.RequestException;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.WeatherApiException;
import ru.sberSchool.tasks.task8.weatherApp.service.WeatherService;
import ru.sberSchool.tasks.task8.weatherApp.util.JsonParser;
import ru.sberSchool.tasks.utils.PropertiesConfigUtil;

import java.io.IOException;

/**
 * Implementation of the {@link WeatherService} interface that fetches weather data
 * for a specified city using an external weather API.
 *
 * @see WeatherService
 *
 * @author Elland Ilia
 */
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    /**
     * {@inheritDoc}
     */
    @Override
    public WeatherResponse getWeatherData(WeatherRequest weatherRequest) throws RequestException, WeatherApiException {
        log.debug("getWeatherData[0]: Getting weather data for city: {}", weatherRequest.getCity());
        OkHttpClient client = new OkHttpClient();

        String url = String.format(
                Constants.WEATHER_URL,
                PropertiesConfigUtil.getProperty(Constants.WEATHER_BASE_URL),
                PropertiesConfigUtil.getProperty(Constants.WEATHER_API_KEY),
                weatherRequest.getCity()
                );

        log.debug("getWeatherData[1]: url: {}", url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RequestException(String.format(Constants.REQUEST_ERROR_MESSAGE, response));
            }

            if (response.body() == null) {
                throw new WeatherApiException(Constants.RESPONSE_BODY_NULL_MESSAGE);
            } else {
                return JsonParser.parseWeatherJson(response.body().string());
            }
        } catch (IOException e) {
            log.error("getWeatherData[0]: error: {}", e.getMessage());
            throw new WeatherApiException(Constants.WEATHER_API_REQUEST_ERROR_MESSAGE);
        }

    }
}
