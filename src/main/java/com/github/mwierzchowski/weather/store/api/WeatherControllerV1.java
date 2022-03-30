package com.github.mwierzchowski.weather.store.api;

import com.github.mwierzchowski.weather.store.core.Weather;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Weather storage resource")
@ApiResponse(description = "Success", responseCode = "200")
@ApiResponse(description = "Bad request", responseCode = "400")
@ApiResponse(description = "Service failure", responseCode = "500")
public class WeatherControllerV1 {
    private final WeatherMapper mapper = Mappers.getMapper(WeatherMapper.class);
    private Weather latest = null;

    @GetMapping("/now")
    @Operation(summary = "Current weather", description = "Provides most current weather")
    public WeatherDto getCurrent() {
        return mapper.weatherDtoFrom(latest);
    }

    @PostMapping
    @Operation(summary = "Save weather", description = "Saves new weather observation")
    public void store(@Valid @RequestBody WeatherDto weatherDto) {
        LOGGER.debug("Received DTO: {}", weatherDto);
        this.latest = mapper.weatherFrom(weatherDto);
        LOGGER.debug("Converted: {}", latest);
    }

    /**
     * Weather mapper to/from DTO.
     */
    @Mapper
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
}
