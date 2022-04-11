package com.github.mwierzchowski.weather.store.web.v1;

import com.github.mwierzchowski.weather.store.core.Temperature;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Schema(description = "Weather observation data")
public class WeatherDto {
    @NotNull
    @PastOrPresent
    @Schema(description = "Observation timestamp in UTC", example = "2022-03-20T21:22:47.685Z")
    private Instant observed;

    @Min(-273)
    @Max(100)
    @Schema(description = "Temperature value", example = "7.0")
    private Double temperature;

    @Schema(description = "Temperature unit", implementation = Temperature.Unit.class,
            example = "C")
    private String temperatureUnit;

    @Min(0)
    @Schema(description = "Wind speed", example = "5.0")
    private Double wind;

    @Schema(description = "Wind speed unit", example = "m/s")
    private String windUnit;

    @Min(0)
    @Max(359)
    @Schema(description = "Wind degree direction", example = "120")
    private Integer windDirection;

    @Min(0)
    @Max(100)
    @Schema(description = "Clouds coverage percentage", example = "10")
    private Integer cloudsCoverage;
}
