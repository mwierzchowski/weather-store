package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents temperature value in given units (see {@link Temperature.Unit}).
 * @author Marcin Wierzchowski
 */

@Entity
@DiscriminatorValue("1")
public class Temperature extends Observation<Temperature.Unit> {
    public Temperature() {
        super(Unit.C);
    }

    @Override
    public void addTo(Weather weather) {
        weather.setTemperature(this);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Unit implements StorageConvertible {
        C(c -> c, c -> c),
        F(f -> (f - 32) * 5 / 9, c -> c * 9 / 5 + 32f),
        K(k -> k - 273.15f, c -> c + 273.15f);

        private final Function<Float, Float> toStorageConverter;
        private final Function<Float, Float> fromStorageConverter;
    }
}
