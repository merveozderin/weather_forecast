package com.example.demo.controller;

import com.example.demo.service.WeatherService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Api(value = "Weather Forecast API Documentation")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    public Mono<String> getForecast(@RequestParam String cityId) {
        return weatherService.getForecast(cityId);
    }
}

