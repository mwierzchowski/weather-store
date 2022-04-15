package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BucketCalculator {
    /**
     * Timestamp of the bucket 0.
     */
    private final long bucketZero;

    /**
     * Bucket size in minutes.
     */
    private final Integer bucketSize;

    public BucketCalculator(
            @NonNull @Value("${weather.bucket.zero:2022-01-01T00:00:00.000Z}") Instant bucketZero,
            @NonNull @Value("${weather.bucket.size:10}") Integer bucketSize) {
        if (bucketZero.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Bucket 0 must be in the past");
        }
        if (bucketSize <= 0) {
            throw new IllegalArgumentException("Bucket size must be bigger than 0");
        }
        this.bucketZero = bucketZero.toEpochMilli();
        this.bucketSize = bucketSize * 60 * 1000;
        LOGGER.info("Initialized with bucket 0 at {} and size of {}min(s)", bucketZero, bucketSize);
    }

    public Long bucketIdFrom(@NonNull Instant timestamp) {
        var timeDiff = timestamp.toEpochMilli() - bucketZero;
        if (timeDiff < 0) {
            throw new IllegalArgumentException("Timestamp must be after bucket 0 begins");
        }
        var bucketId = timeDiff / bucketSize;
        LOGGER.debug("Timestamp {} belongs to bucket {}", timestamp, bucketId);
        return bucketId;
    }

    public Instant timestampFrom(@NonNull Long bucketId) {
        if (bucketId < 0) {
            throw new IllegalArgumentException("Bucket id must be bigger or equal zero");
        }
        var timestamp = Instant.ofEpochMilli(bucketZero + bucketId * bucketSize);
        LOGGER.debug("Bucket {} starts at {}", bucketId, timestamp);
        return timestamp;
    }
}
