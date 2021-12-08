package com.makotovh.counter.repository;

import com.makotovh.counter.model.Counter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CounterRepository extends ReactiveCrudRepository<Counter, Long> {

    Mono<Counter> findByName(String name);
}
