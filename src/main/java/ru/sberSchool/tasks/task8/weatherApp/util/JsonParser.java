package ru.sberSchool.tasks.task8.weatherApp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherResponse;

/**
 * Utility class for parsing JSON data into Java objects.
 *
 * @author Elland Ilia
 */
@Slf4j
public class JsonParser {

    /**
     * Parses the provided JSON data into a {@link WeatherResponse} object.
     * <p>
     * This method takes a JSON string, deserializes it using the Jackson {@link ObjectMapper},
     * and returns the corresponding {@link WeatherResponse} object.
     * </p>
     *
     * @param jsonData the JSON string to be parsed, containing weather data
     * @return a {@link WeatherResponse} object representing the weather data
     * @throws JsonProcessingException if there is an error processing the JSON data
     */
    public static WeatherResponse parseWeatherJson(String jsonData) throws JsonProcessingException {
        log.debug("parseWeatherJson[0]: Parse data from json: {}", jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonData, WeatherResponse.class);
    }

}
