package com.github.mwierzchowski.weather.store.api;

import com.github.mwierzchowski.weather.store.core.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherControllerV1 {
    private Weather latest = null;

    @GetMapping("/now")
    public Weather getLatest() {
        return latest;
    }

    @PostMapping
    public void store(@Valid @RequestBody Weather weather) {
        log.debug("Received: {}", weather);
        this.latest = weather;
    }
}
