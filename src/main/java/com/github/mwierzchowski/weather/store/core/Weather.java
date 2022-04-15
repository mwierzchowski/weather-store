package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
import java.util.stream.Stream;
import lombok.Data;

/**
 * Represents weather observations.
 * @author Marcin Wierzchowski
 */
@Data
public class Weather {
    /**
     * Timestamp of weather observation.
     */
    private Instant observed;

    /**
     * Temperature.
     */
    private Temperature temperature;

    /**
     * Wind.
     */
    private Wind wind;

    private WindDirection windDirection;

    /**
     * Clouds coverage (percentage).
     */
    private CloudsCoverage cloudsCoverage;

    public Stream<Observation<? extends StorageConvertible>> observations() {
        return Stream.of(temperature, wind, windDirection, cloudsCoverage);
    }
}
