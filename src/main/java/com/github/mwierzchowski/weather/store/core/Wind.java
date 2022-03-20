package com.github.mwierzchowski.weather.store.core;

import lombok.Data;

/**
 * Represents wind vector made of speed and direction.
 * @author Marcin Wierzchowski
 */
@Data
public class Wind {
    /**
     * Speed of wind.
     */
    private Speed speed;

    /**
     * Direction degree of wind (0...359).
     */
    private Integer direction;
}
