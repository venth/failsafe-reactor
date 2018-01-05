package com.github.venth.failsafe.reactor;

import java.util.function.Function;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.CircuitBreakerOpenException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CircuitBreakerOperator<T> implements Publisher<T>, Subscriber<T> {

    private final Publisher<T> flux;

    private final CircuitBreaker circuitBreaker;

    private Subscriber<? super T> subscriber;

    CircuitBreakerOperator(Publisher<T> flux, CircuitBreaker circuitBreaker) {
        this.flux = flux;
        this.circuitBreaker = circuitBreaker;
    }

    public static <T> Function<Publisher<T>, Publisher<T>> of(CircuitBreaker circuitBreaker) {
        return flux -> new CircuitBreakerOperator<T>(flux, circuitBreaker);
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;
        if (circuitBreaker.allowsExecution()) {
            flux.subscribe(this);
        } else {
            subscriber.onError(new CircuitBreakerOpenException());
        }

    }

    @Override
    public void onSubscribe(Subscription s) {
        subscriber.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        subscriber.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        circuitBreaker.recordFailure(t);
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        circuitBreaker.recordSuccess();
        subscriber.onComplete();
    }
}
