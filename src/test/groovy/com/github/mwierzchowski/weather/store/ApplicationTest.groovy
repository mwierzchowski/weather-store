package com.github.mwierzchowski.weather.store

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class ApplicationTest extends Specification {
    @Subject
    @Autowired
    Application application

    def "Application should start"() {
        expect:
        application != null
    }
}
