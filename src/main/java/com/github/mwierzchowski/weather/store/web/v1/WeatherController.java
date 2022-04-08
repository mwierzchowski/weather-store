package com.github.mwierzchowski.weather.store.web.v1;

import com.github.mwierzchowski.weather.store.core.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Weather store resource")
@AllArgsConstructor
@Slf4j
public class WeatherController {
    private final WeatherMapper mapper;
    private final WeatherService service;

    @PostMapping
    @Operation(summary = "Submit weather",
            description = "Submits current weather observation to the store")
    @ApiResponse(responseCode = "200", description = "Weather observation successfully submitted")
    @ApiResponse(responseCode = "400", description = "Weather data is invalid")
    public void submit(@Valid @RequestBody WeatherDto weatherDto) {
        LOGGER.debug("Submitting weather: {}", weatherDto);
        var weather = mapper.weatherFrom(weatherDto);
        service.submit(weather);
        LOGGER.debug("Weather submitted: {}", weather);
    }

    @GetMapping("/{time}")
    @Operation(summary = "Retrieve weather",
            description = "Retrieves weather observed on given time")
    @ApiResponse(responseCode = "200", description = "Weather observation successfully retrieved")
    @ApiResponse(responseCode = "204", description = "Weather observation not available")
    public WeatherDto retrieve(
            @PathParam("time")
            @Parameter(description = "UTC time of observation",
                    example = "2022-03-20T21:22:47.685Z") Date date) throws NoSuchElementException {
        LOGGER.debug("Retrieving weather for {}", date);
        var dateTime = date.toInstant().atZone(ZoneId.of("UTC")); // TODO Date should have zone
        var weather = service.findFor(dateTime).orElseThrow();
        var weatherDto = mapper.weatherDtoFrom(weather);
        LOGGER.debug("Weather retrieved: {}", weatherDto);
        return weatherDto;
    }
}
