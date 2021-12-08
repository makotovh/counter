package com.makotovh.counter.service;

import com.makotovh.counter.exception.CounterAlreadyExistsException;
import com.makotovh.counter.exception.CounterNotFoundException;
import com.makotovh.counter.model.Counter;
import com.makotovh.counter.repository.CounterRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Data
@AllArgsConstructor
@Getter(AccessLevel.NONE)
public class CounterService {
    private final CounterRepository counterRepository;

    public Mono<Counter> create(String counterName) {
        return counterRepository.findByName(counterName)
                .flux().count()
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.error(new CounterAlreadyExistsException());
                    }
                    return counterRepository.save(new Counter(null, counterName, 0l));
                });
    }

    public Flux<Counter> findAll() {
        return counterRepository.findAll();
    }

    public Mono<Counter> findByName(String counterName) {
        return counterRepository.findByName(counterName)
                .switchIfEmpty(Mono.error(new CounterNotFoundException()));
    }

    public Mono<Counter> increment(String name) {
        return counterRepository.findByName(name)
                .switchIfEmpty(Mono.error(new CounterNotFoundException()))
                .map(Counter::increment)
                .flatMap(counterRepository::save);
    }
}
