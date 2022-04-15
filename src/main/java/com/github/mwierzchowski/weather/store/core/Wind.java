package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;
import java.util.stream.Stream;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents speed value in given units (see {@link Unit}).
 * @author Marcin Wierzchowski
 */
@Entity
@DiscriminatorValue("2")
public class Wind extends Observation<Wind.Unit> {
    public Wind() {
        super(Unit.M_PER_S);
    }

    @Override
    public void addTo(Weather weather) {
        weather.setWind(this);
    }

    /**
     * Units of speed.
     */
    @Getter
    @RequiredArgsConstructor
    public enum Unit implements StorageConvertible {
        M_PER_S("m/s", m -> m, m -> m),
        KM_PER_H("km/h", k -> k / 3.6f, m -> m * 3.6f),
        MI_PER_H("mph", mi -> mi / 2.23693629f, m -> m * 2.23693629f);

        private final String symbol;
        private final Function<Float, Float> toStorageConverter;
        private final Function<Float, Float> fromStorageConverter;

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
                    .orElseThrow(() ->
                            new IllegalArgumentException("Symbol " + symbol + " is not valid")
                    );
        }
    }
}
