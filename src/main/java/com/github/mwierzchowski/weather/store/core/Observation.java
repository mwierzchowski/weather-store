package com.github.mwierzchowski.weather.store.core;

import java.util.Objects;
import java.util.function.Function;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import static javax.persistence.DiscriminatorType.INTEGER;

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
public abstract class Observation<U extends BaseConvertible> {
    @Id
    private Long bucketId;

    private Float value;

    @Transient
    private final U baseUnit;

    @Transient
    private U unit;

    @Version
    private Short version;

    @PreUpdate
    @PrePersist
    public void normalize() {
        LOGGER.debug("Normalizing {} to {}", getClass().getSimpleName(), baseUnit);
        convertTo(baseUnit);
    }

    @PostLoad
    public void initialize() {
        LOGGER.debug("Initializing {} with {}", getClass().getSimpleName(), baseUnit);
        unit = baseUnit;
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
            var baseValue = unit.getToBaseConverter().apply(value);
            value = otherUnit.getFromBaseConverter().apply(baseValue);
        }
        unit = otherUnit;
        LOGGER.debug("Converted {} {}{} to {}{}", getClass().getSimpleName(),
                oldValue, oldUnit, value, unit);
    }

    public boolean isNotEmpty() {
        return value != null;
    }

    public abstract void addTo(Weather weather);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Observation<?> that = (Observation<?>) o;
        return bucketId != null && Objects.equals(bucketId, that.bucketId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
