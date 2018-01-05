package com.github.venth.failsafe.reactor

import java.time.Duration
import java.util.concurrent.Callable

import net.jodah.failsafe.CircuitBreaker
import net.jodah.failsafe.CircuitBreakerOpenException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

class CircuitBreakerOperatorTest extends Specification {

    def "flux finishes with CircuitBreakerOpenException and no call is made when the breaker is open"() {
        when:
            Flux.defer({ Flux.fromIterable(remoteCall.call()) })
                    .transform(CircuitBreakerOperator.of(openCircuitBreaker))
                    .blockLast(testTimeout)
        then:
            thrown CircuitBreakerOpenException
        and:
            0 * remoteCall.call()
    }

    def "flux makes call and passes its results when circuit breaker is closed"() {
        when:
            def results = Flux.defer({ Flux.fromIterable(remoteCall.call()) })
                    .transform(CircuitBreakerOperator.of(closedCircuitBreaker))
                    .collectList()
                    .block(testTimeout)
        then:
            results == successResults
    }

    def "mono finishes with CircuitBreakerOpenException and no call is made when the breaker is open"() {
        when:
            Mono.fromCallable(remoteCall)
                    .transform(CircuitBreakerOperator.of(openCircuitBreaker))
                    .block(testTimeout)
        then:
            thrown CircuitBreakerOpenException
        and:
            0 * remoteCall.call()
    }

    def "mono makes call and passes its results when circuit breaker is closed"() {
        when:
            def results = Mono.fromCallable(remoteCall)
                    .transform(CircuitBreakerOperator.of(closedCircuitBreaker))
                    .block(testTimeout)
        then:
            results == successResults
    }


    def testTimeout = Duration.ofMillis(20)
    def successResult = new Object()
    def successResults = [successResult, successResult, successResult]
    def remoteCall = Mock(Callable) {
        call() >> { successResults }
    }

    def openCircuitBreaker = Mock(CircuitBreaker) {
        allowsExecution() >> false
    }
    def closedCircuitBreaker = Mock(CircuitBreaker) {
        allowsExecution() >> true
    }
}
