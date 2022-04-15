package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("4")
public class CloudsCoverage extends Observation<CloudsCoverage.Unit> {
    public CloudsCoverage() {
        super(Unit.PERCENTAGE);
    }

    @Override
    public void addTo(Weather weather) {
        weather.setCloudsCoverage(this);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Unit implements StorageConvertible {
        PERCENTAGE(p -> p, p -> p);

        private final Function<Float, Float> toStorageConverter;
        private final Function<Float, Float> fromStorageConverter;
    }
}
