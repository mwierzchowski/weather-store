package com.github.mwierzchowski.weather.store.core;

import java.util.function.Function;

public interface StorageConvertible {
    Function<Float, Float> getToStorageConverter();

    Function<Float, Float> getFromStorageConverter();
}
