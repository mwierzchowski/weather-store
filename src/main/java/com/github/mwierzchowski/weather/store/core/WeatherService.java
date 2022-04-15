package com.github.mwierzchowski.weather.store.core;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final BucketCalculator bucketCalculator;
    private final UnitService unitService;

    public void submit(Weather weather) {
        var bucketId = bucketCalculator.bucketIdFrom(weather.getObserved());
        LOGGER.debug("Persisting weather to bucket {}", bucketId);
        weather.observations().filter(Observation::isNotEmpty).forEach(observation -> {
            observation.setBucketId(bucketId);
            observation.useDefaultIfMissingUnit(unitService::defaultUnit);
            entityManager.persist(observation);
            LOGGER.debug("Persisting {} observation", observation.getClass().getSimpleName());
        });
    }

    @SuppressWarnings("unchecked")
    public Optional<Weather> findFor(Instant timestamp) {
        var bucketId = bucketCalculator.bucketIdFrom(timestamp);
        LOGGER.debug("Searching for weather observations in bucket {}", bucketId);
        var observations = entityManager
                .createNamedQuery("Observation.findByBucketId", Observation.class)
                .setParameter("bucketId", bucketId)
                .getResultList();
        entityManager.clear();
        LOGGER.debug("Found {} observation(s) for bucket {}", observations.size(), bucketId);
        if (observations.isEmpty()) {
            return Optional.empty();
        }
        var weather = new Weather();
        weather.setObserved(timestamp);
        observations.forEach(observation -> {
            observation.convertTo(unitService.defaultUnit(observation));
            observation.addTo(weather);
        });
        return Optional.of(weather);
    }
}
