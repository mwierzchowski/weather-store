package com.github.mwierzchowski.weather.store.core

import spock.lang.Specification

import static com.github.mwierzchowski.weather.store.core.Temperature.Unit.C
import static com.github.mwierzchowski.weather.store.core.Temperature.Unit.F
import static com.github.mwierzchowski.weather.store.core.Temperature.Unit.K

class TemperatureSpec extends Specification {
    def "Converts between units"() {
        given:
        Temperature temperature = new Temperature().tap {
            value = v1
            unit = u1
        }
        when:
        temperature.convertTo(u2)
        then:
        temperature.value.round(1) == v2.floatValue().round(1)
        where:
          v1 | u1 ||     v2 | u2
         -40 |  C ||  -40.0 |  F
         -30 |  C ||  -22.0 |  F
           0 |  C ||   32.0 |  F
          25 |  C ||   77.0 |  F
         -40 |  C ||  233.1 |  K
         -33 |  C ||  240.1 |  K
           0 |  C ||  273.1 |  K
          25 |  C ||  298.1 |  K
         -40 |  F ||  -40.0 |  C
         -10 |  F ||  -23.3 |  C
           0 |  F ||  -17.8 |  C
          40 |  F ||    4.4 |  C
         -40 |  F ||  233.1 |  K
         -10 |  F ||  249.8 |  K
           0 |  F ||  255.4 |  K
          40 |  F ||  277.6 |  K
           0 |  K || -273.1 |  C
           0 |  K || -459.7 |  F
          59 |  K || -214.1 |  C
          59 |  K || -353.5 |  F
    }
}
