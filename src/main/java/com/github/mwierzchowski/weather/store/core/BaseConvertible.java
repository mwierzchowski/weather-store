package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;

public interface BaseConvertible {
    Function<Float, Float> getToBaseConverter();
    Function<Float, Float> getFromBaseConverter();
}
