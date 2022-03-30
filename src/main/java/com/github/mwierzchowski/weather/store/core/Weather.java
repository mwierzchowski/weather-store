package com.github.mwierzchowski.weather.store.core;

import static com.github.mwierzchowski.weather.store.core.Temperature.Unit.C;
import java.time.Instant;
import static java.time.Instant.now;
import static java.time.Period.ofDays;
import lombok.Data;

/**
 * Represents weather conditions.
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

    /**
     * Clouds coverage (percentage).
     */
    private Integer cloudsCoverage;

    public static void main(String[] args) {

        var unit = C;
        now();
        ofDays(0);
    }
}
