package com.github.mwierzchowski.weather.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ApplicationTest extends Specification {
    @Autowired
    Application application

    def "Application should start"() {
        expect:
        application != null
    }
}
