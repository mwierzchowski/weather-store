package com.github.mwierzchowski.weather.store.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnitService {
    <U extends BaseConvertible> U defaultUnit(@NonNull Observation<U> observation) {
        return (U) Temperature.Unit.K;
    }
}
