package ru.sberSchool.tasks.task8.weatherApp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherRequest;
import ru.sberSchool.tasks.task8.weatherApp.dto.WeatherResponse;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.RequestException;
import ru.sberSchool.tasks.task8.weatherApp.exceptions.WeatherApiException;
import ru.sberSchool.tasks.task8.weatherApp.service.impl.WeatherServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WeatherServiceTest {

    private final WeatherServiceImpl weatherService = new WeatherServiceImpl();

    @Test
    void getWeatherData_success() throws Exception {
        log.debug("getWeatherData_success[0]: Starting positive test case");

        WeatherRequest request = new WeatherRequest("London");
        WeatherResponse response = weatherService.getWeatherData(request);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.getCurrent().getTemp_c() > -50 && response.getCurrent().getTemp_c() < 60, "Temperature should be within a reasonable range");
        assertNotNull(response.getCurrent().getCondition().getText(), "Condition text should not be null");

        log.debug("getWeatherData_success[1]: Response validation passed");
    }

    @Test
    void getWeatherData_invalidCity() {
        log.debug("getWeatherData_invalidCity[0]: Starting negative test case with invalid city");

        WeatherRequest request = new WeatherRequest("InvalidCity");

        RequestException exception = assertThrows(RequestException.class, () ->
                weatherService.getWeatherData(request)
        );

        assertNotNull(exception.getMessage(), "Exception message should not be null");
        log.debug("getWeatherData_invalidCity[1]: Exception validation passed");
    }
}
