package com.github.mwierzchowski.weather.store.web.v1;

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
    @Mapping(target = "windUnit",
            expression = "java( weather.getWind().getSpeed().getUnit().getSymbol() )")
    @Mapping(target = "windDirection", source = "wind.direction")
    WeatherDto weatherDtoFrom(Weather weather);

    @InheritInverseConfiguration
    @Mapping(target = "wind.speed.unit",
            expression = "java( Speed.Unit.from(weatherDto.getWindUnit()) )")
    Weather weatherFrom(WeatherDto weatherDto);
}
