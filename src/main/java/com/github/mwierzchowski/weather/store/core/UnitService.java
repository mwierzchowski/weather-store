package com.github.mwierzchowski.weather.store.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnitService {
    @SuppressWarnings("unchecked")
    <U extends StorageConvertible> U defaultUnit(@NonNull Observation<U> observation) {
        var unit = switch (observation.getClass().getSimpleName()) {
            case "Temperature" -> Temperature.Unit.C;
            case "Wind" -> Wind.Unit.KM_PER_H;
            case "WindDirection" -> WindDirection.Unit.DEGREES;
            case "CloudsCoverage" -> CloudsCoverage.Unit.PERCENTAGE;
            default -> null;
        };
        return (U) unit;
    }
}
