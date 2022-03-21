package com.github.mwierzchowski.weather.store.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * Represents speed value in given units (see {@link Unit}).
 * @author Marcin Wierzchowski
 */
@Data
public class Speed {
    /**
     * Speed value.
     */
    private BigDecimal value;

    /**
     * Speed unit (see {@link Unit}).
     */
    private Unit unit;

    /**
     * Units of speed
     */
    @AllArgsConstructor
    public enum Unit {
        KM_PER_H("km/h"),
        M_PER_S("m/s"),
        MI_PER_H("mph");

        /**
         * Symbol of unit.
         */
        @Getter
        private final String symbol;

        @Override
        public String toString() {
            return symbol;
        }

        public static Unit from(String symbol) {
            if (symbol == null) {
                return null;
            }
            return Stream.of(values())
                    .filter(unit -> unit.symbol.equals(symbol))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Symbol " + symbol + " is not valid"));
        }
    }
}
