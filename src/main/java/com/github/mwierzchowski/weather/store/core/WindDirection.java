package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("3")
public class WindDirection extends Observation<WindDirection.Unit> {
    public WindDirection() {
        super(Unit.DEGREES);
    }

    @Override
    public void addTo(Weather weather) {
        weather.setWindDirection(this);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Unit implements StorageConvertible {
        DEGREES(d -> d, d -> d);

        private final Function<Float, Float> toStorageConverter;
        private final Function<Float, Float> fromStorageConverter;
    }
}
