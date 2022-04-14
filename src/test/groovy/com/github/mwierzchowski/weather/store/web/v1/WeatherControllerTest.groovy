package com.github.mwierzchowski.weather.store.web.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

import java.time.Instant

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
class WeatherControllerTest extends Specification {
    @Autowired
    TestRestTemplate restTemplate

    String baseUrl = "/api/v1/weather/"

    def "Submit succeeds if valid weather data"() {
        given:
        def weatherDto = new WeatherDto().tap {
            observed = Instant.now()
            temperature = 22.5
        }
        when:
        var result = restTemplate.postForEntity(baseUrl, weatherDto, String)
        then:
        result.getStatusCodeValue() == 200
    }

    def "Submit fails if invalid weather data"() {
        given:
        def weatherDto = new WeatherDto()
        when:
        var result = restTemplate.postForEntity(baseUrl, weatherDto, String)
        then:
        result.getStatusCodeValue() == 400
    }

    def "Retrieve weather if available"() {
        given:
        def time = Instant.now()
        def weatherDto = new WeatherDto().tap {
            observed = time
            temperature = 22.5
        }
        restTemplate.postForEntity(baseUrl, weatherDto, String.class)
        when:
        var response = restTemplate.getForEntity(baseUrl + time, WeatherDto)
        then:
        response.getStatusCodeValue() == 200
        response.getBody().temperature == weatherDto.temperature
    }

    def "Retrieve empty weather if not available"() {
        given:
        def time = Instant.now().plusMillis(1000000)
        when:
        var response = restTemplate.getForEntity(baseUrl + time, WeatherDto)
        then:
        response.getStatusCodeValue() == 204
    }
}