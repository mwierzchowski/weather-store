package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    protected Weather weather;

    public void submit(Weather weather) {
        // TODO dummy implementation
        this.weather = weather;
    }

    public Optional<Weather> findFor(Instant time) {
        // TODO dummy implementation
        if (weather == null || !time.equals(weather.getObserved())) {
            return Optional.empty();
        }
        return Optional.of(weather);
    }
}
