package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BucketCalculator {
    /**
     * Timestamp of the bucket zero.
     */
    private final long bucketZero;

    /**
     * Bucket size in minutes.
     */
    private final Integer bucketSize;

    public BucketCalculator(
            @Value("${weather.bucket.zero:2022-01-01T00:00:00.000Z}") Instant bucketZero,
            @Value("${weather.bucket.size:10}") Integer bucketSize) {
        if (bucketZero.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Bucket zero must be in the past");
        }
        if (bucketSize <= 0) {
            throw new IllegalArgumentException("Bucket size must be bigger than 0 mins");
        }
        this.bucketZero = bucketZero.toEpochMilli();
        this.bucketSize = bucketSize * 60 * 1000;
        LOGGER.info("Using bucket zero at {} and size of {}min", bucketZero, bucketSize);
    }

    public Long bucketFrom(Instant timestamp) {
        var timeDiff = timestamp.toEpochMilli() - bucketZero;
        if (timeDiff < 0) {
            throw new IllegalArgumentException("Timestamp must be after bucket zero begins");
        }
        var bucket = timeDiff / bucketSize;
        LOGGER.debug("Timestamp {} belongs to bucket {}", timestamp, bucket);
        return bucket;
    }

    public Instant timestampFrom(Long bucket) {
        if (bucket < 0) {
            throw new IllegalArgumentException("Bucket must be bigger or equal zero");
        }
        var timestamp = Instant.ofEpochMilli(bucketZero + bucket * bucketSize);
        LOGGER.debug("Bucket {} started at {}", bucket, timestamp);
        return timestamp;
    }
}
