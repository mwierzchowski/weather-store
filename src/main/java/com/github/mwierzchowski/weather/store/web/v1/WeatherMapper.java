package com.github.mwierzchowski.weather.store.web.v1;

import com.github.mwierzchowski.weather.store.core.Speed;
import com.github.mwierzchowski.weather.store.core.Weather;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Weather mapper to/from DTO.
 */
@Mapper(componentModel = "spring")
interface WeatherMapper {
    @Mapping(target = "temperature", source = "temperature.value")
    @Mapping(target = "temperatureUnit", source = "temperature.unit")
    @Mapping(target = "wind", source = "wind.speed.value")
    @Mapping(target = "windUnit", source = "wind.speed.unit")
    @Mapping(target = "windDirection", source = "wind.direction")
    WeatherDto weatherDtoFrom(Weather weather);

    @InheritInverseConfiguration
    Weather weatherFrom(WeatherDto weatherDto);

    default String speedUnitSymbolFrom(Speed.Unit unit) {
        return unit != null ? unit.getSymbol() : null;
    }

    default Speed.Unit speedUnitFrom(String symbol) {
        return Speed.Unit.from(symbol);
    }
}
