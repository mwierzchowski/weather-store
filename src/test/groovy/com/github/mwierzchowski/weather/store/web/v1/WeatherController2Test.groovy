package com.github.mwierzchowski.weather.store.web.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Ignore
import spock.lang.Specification

import java.time.Instant

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

//@Ignore
@SpringBootTest(webEnvironment = RANDOM_PORT)
class WeatherController2Test extends Specification {
    @Autowired
    TestRestTemplate restTemplate

    String baseUrl = "/api/v1/weather/"

    def "Submit succeeds if valid weather observation"() {
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

    def "Submit fails if invalid weather observation"() {
        given:
        def weatherDto = new WeatherDto()
        when:
        var result = restTemplate.postForEntity(baseUrl, weatherDto, String)
        then:
        result.getStatusCodeValue() == 400
    }

    def "Retrieve weather observation if available"() {
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
        response.getBody() == weatherDto
    }

    def "Retrieve empty weather observation if not available"() {
        given:
        def time = Instant.now().minusMillis(1000)
        when:
        var response = restTemplate.getForEntity(baseUrl + time, WeatherDto)
        then:
        response.getStatusCodeValue() == 204
    }
}
