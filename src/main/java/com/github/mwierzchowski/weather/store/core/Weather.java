package com.github.mwierzchowski.weather.store.core;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;

/**
 * Represents weather conditions.
 * @author Marcin Wierzchowski
 */
@Data
public class Weather {
    /**
     * Timestamp of weather observation.
     */
    @PastOrPresent
    private Instant timestamp;

    /**
     * Temperature
     */
    private Temperature temperature;

    /**
     * Wind
     */
    private Wind wind;

    /**
     * Clouds coverage (percentage)
     */
    @Min(0)
    @Max(100)
    private Integer cloudsCoverage;
}
