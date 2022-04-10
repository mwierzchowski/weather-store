package com.github.mwierzchowski.weather.store.core

import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant

import static java.time.temporal.ChronoUnit.SECONDS

class BucketCalculatorTest extends Specification {
    @Shared
    def now = Instant.now()

    def "Calculates bucket from timestamp"() {
        given:
        var calculator = new BucketCalculator(zero, size)

        expect:
        calculator.bucketFrom(timestamp) == bucket

        where:
        zero         | size | timestamp    || bucket
        nowMinus(60) |   10 | nowMinus(60) ||      0
        nowMinus(60) |   10 | nowMinus(54) ||      0
        nowMinus(60) |   10 | nowMinus(50) ||      1
        nowMinus(60) |   10 | nowMinus(49) ||      1
        nowMinus(30) |    5 | nowMinus(1)  ||      5
        nowMinus(30) |    5 | nowMinus(0)  ||      6
    }

    def "Calculates timestamp from bucket"() {
        given:
        var calculator = new BucketCalculator(zero, size)

        expect:
        calculator.timestampFrom(bucket).truncatedTo(SECONDS) == timestamp.truncatedTo(SECONDS)

        where:
        zero         | size | bucket || timestamp
        nowMinus(60) |   10 |      0 || nowMinus(60)
        nowMinus(60) |    5 |      1 || nowMinus(55)
        nowMinus(30) |    5 |      6 || nowMinus(0)
        nowMinus(30) |   10 |      3 || nowMinus(0)
    }

    def "Constructor fails if bucket zero is in the future"() {
        given:
        def bucketZero = now.plusMillis(10000)
        def bucketSize = 10

        when:
        new BucketCalculator(bucketZero, bucketSize)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.containsIgnoreCase("bucket zero")
    }

    def "Constructor fails if bucket size is less or equal zero"() {
        given:
        def zero = nowMinus(10)

        when:
        new BucketCalculator(zero, size)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.containsIgnoreCase("bucket size")

        where:
        size | _
           0 | _
        -100 | _
    }

    def "Bucket calculation fails if timestamp is before bucket zero begins"() {
        when:
        new BucketCalculator(nowMinus(100), 10).bucketFrom(nowMinus(101))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.containsIgnoreCase("timestamp")
    }

    def "Timestamp calculation fails if bucket is less than zero"() {
        when:
        new BucketCalculator(nowMinus(0), 10).timestampFrom(-1)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.containsIgnoreCase("bucket")
    }

    def nowMinus(minutes) {
        now.minusMillis(minutes * 60 * 1000)
    }
}