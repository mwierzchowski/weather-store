package com.github.mwierzchowski.weather.store.web.v1

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.Instant

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WeatherControllerTest extends Specification {
    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    def "Submit succeeds if valid weather observation"() {
        given:
        def weatherDto = new WeatherDto().tap {
            observed = Instant.now()
            temperature = 22.5
        }
        expect:
        mvc.perform(post("/api/v1/weather")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weatherDto)))
                .andExpect(status().isOk())
    }

    def "Submit fails if invalid weather observation"() {
        given:
        def weatherDto = new WeatherDto()
        expect:
        mvc.perform(post("/api/v1/weather")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weatherDto)))
                .andExpect(status().isBadRequest())
    }

    def "Retrieve weather observation if available"() {
        given:
        def time = Instant.now()
        def weatherDto1 = new WeatherDto().tap {
            observed = time
            temperature = 22.5
        }
        expect:
        mvc.perform(post("/api/v1/weather")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weatherDto1)))
                .andExpect(status().isOk())
        var response = mvc.perform(get("/api/v1/weather/" + time)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
        var weatherDto2 = objectMapper.readValue(response, WeatherDto)
        weatherDto1 == weatherDto2
    }

    def "Retrieve empty weather observation if not available"() {
        given:
        def time = Instant.now().minusMillis(1000)
        expect:
        mvc.perform(get("/api/v1/weather/" + time)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
    }
}
