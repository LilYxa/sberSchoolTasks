package ru.sberSchool.tasks.task8.weatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRequest {

    @NonNull
    private String city;
}
