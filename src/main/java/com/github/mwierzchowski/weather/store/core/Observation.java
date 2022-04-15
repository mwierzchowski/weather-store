package com.github.mwierzchowski.weather.store.core;

import java.util.Objects;
import java.util.function.Function;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import static javax.persistence.DiscriminatorType.INTEGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@Inheritance
@RequiredArgsConstructor
@DiscriminatorColumn(discriminatorType = INTEGER)
@NamedQuery(name = "Observation.findByBucketId",
        query = "SELECT o FROM Observation o WHERE o.bucketId = :bucketId")
public abstract class Observation<U extends StorageConvertible> {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator="observation_id_seq")
    @SequenceGenerator(name="observation_id_seq", sequenceName="observation_id_seq")
    private Long id;

    private Long bucketId;

    private Float value;

    @Transient
    private final U storageUnit;

    @Transient
    private U unit;

    @PreUpdate
    @PrePersist
    public void normalize() {
        LOGGER.debug("Normalizing {} to storage unit: {}", getClass().getSimpleName(), storageUnit);
        convertTo(storageUnit);
    }

    @PostLoad
    public void initialize() {
        LOGGER.debug("Initializing {} with storage unit: {}",
                getClass().getSimpleName(), storageUnit);
        unit = storageUnit;
    }

    public void useDefaultIfMissingUnit(Function<Observation<U>, U> defaultUnitProvider) {
        if (unit != null) {
            return;
        }
        unit = defaultUnitProvider.apply(this);
        LOGGER.debug("Missing {} unit, using default: {}", getClass().getSimpleName(), unit);
    }

    public void convertTo(@NonNull U otherUnit) {
        if (unit == null) {
            throw new IllegalStateException("Unit can not be null");
        }
        if (unit == otherUnit) {
            LOGGER.debug("Conversion unit {} is already used", unit);
            return;
        }
        var oldValue = value;
        var oldUnit = unit;
        if (isNotEmpty()) {
            var baseValue = unit.getToStorageConverter().apply(value);
            value = otherUnit.getFromStorageConverter().apply(baseValue);
        }
        unit = otherUnit;
        LOGGER.debug("Converted {} {} {} to {} {}", getClass().getSimpleName(),
                oldValue, oldUnit, value, unit);
    }

    public boolean isNotEmpty() {
        return value != null;
    }

    public abstract void addTo(Weather weather);

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
            return false;
        }
        Observation<?> that = (Observation<?>) other;
        return bucketId != null && Objects.equals(bucketId, that.bucketId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
