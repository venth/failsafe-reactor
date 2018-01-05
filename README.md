# Circuit Breaker Operator for Spring Reactor
[![Build Status](https://travis-ci.org/venth/failsafe-reactor.svg?branch=master)](https://travis-ci.org/venth/failsafe-reactor)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.venth.failsafe/reactor.svg?style=plastic)]()
Since introduction of [Spring Reactive](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/web-reactive.html#spring-webflux)
appeared a need of a circuit breaker that would follow the reactive principles and is very light. 

[FailSafe](https://github.com/jhalterman/failsafe) provides very light circuit breaker which works perfectly with
Callable and Runnable and is complicated when comes to an application on an observable sequence.

This project brings new spring reactor circuit breaker operator based on [FailSafe](https://github.com/jhalterman/failsafe)
which aims to be easy to use. 

# Usage

## gradle

```gradle
compile 'com.github.venth.failsafe:reactor:x.y.z'
```

## maven

```maven
<dependency>
    <groupId>com.github.venth.failsafe</groupId>
    <artifactId>reactor</artifactId>
    <version>x.y.z</version>
</dependency>
```

Usage for each of the ReactiveX observables type is pretty similar. The examples presented below
shall explain the usage.

* xxxSequence - a sequence that depends on the operator's type.
* circuitBreaker is configured instance of [FailSafe](https://github.com/jhalterman/failsafe) CircuitBreaker.

## Flux

```
fluxSequence.transform(CircuitBreakerOperator.of(circuitBreaker))
```

## Mono

```
monoSequence.transform(CircuitBreakerOperator.of(circuitBreaker))
```
