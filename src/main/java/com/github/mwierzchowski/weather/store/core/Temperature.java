package com.github.mwierzchowski.weather.store.core;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Represents temperature value in given units (see {@link Unit}).
 * @author Marcin Wierzchowski
 */
@Data
public class Temperature {
    /**
     * Temperature value.
     */
    private BigDecimal value;

    /**
     * Temperature unit (see {@link Unit}).
     */
    private Unit unit;

    /**
     * Units of temperature
     */
    public enum Unit {
        C, F, K
    }
}
