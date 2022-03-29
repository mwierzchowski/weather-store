package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
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
}
