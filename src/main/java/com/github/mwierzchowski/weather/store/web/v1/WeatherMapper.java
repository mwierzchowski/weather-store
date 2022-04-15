package com.github.mwierzchowski.weather.store.web.v1;

import com.github.mwierzchowski.weather.store.core.Wind;
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
    @Mapping(target = "wind", source = "wind.value")
    @Mapping(target = "windUnit", source = "wind.unit")
    @Mapping(target = "windDirection", source = "windDirection.value")
    @Mapping(target = "cloudsCoverage", source = "cloudsCoverage.value")
    WeatherDto weatherDtoFrom(Weather weather);

    @InheritInverseConfiguration
    Weather weatherFrom(WeatherDto weatherDto);

    default String speedUnitSymbolFrom(Wind.Unit unit) {
        if (unit == null) {
            return null;
        }
        return unit.getSymbol();
    }

    default Wind.Unit speedUnitFrom(String symbol) {
        return Wind.Unit.from(symbol);
    }
}
