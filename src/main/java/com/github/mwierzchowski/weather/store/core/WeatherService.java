package com.github.mwierzchowski.weather.store.core;

import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WeatherService {
    public void submit(Weather weather) {
        // TODO
    }

    public Optional<Weather> findFor(ZonedDateTime time) {
        // TODO
        return null;
    }
}
