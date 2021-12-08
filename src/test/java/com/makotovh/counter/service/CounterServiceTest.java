package com.makotovh.counter.service;

import com.makotovh.counter.exception.CounterAlreadyExistsException;
import com.makotovh.counter.exception.CounterNotFoundException;
import com.makotovh.counter.model.Counter;
import com.makotovh.counter.repository.CounterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class CounterServiceTest {

    @Mock
    private CounterRepository counterRepository;

    @InjectMocks
    private CounterService counterService;

    @Test
    public void shouldCreate() {
        String name = "test";
        when(counterRepository.findByName(name)).thenReturn(Mono.empty());
        Counter expectedCounter = new Counter(null, name, 0l);
        when(counterRepository.save(expectedCounter)).thenReturn(Mono.just(expectedCounter));
        StepVerifier.create(counterService.create(name))
                .expectNextMatches(counter -> {
                    assertThat(counter).isEqualTo(expectedCounter);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldNotAllowDuplicatedCounterName() {
        String existentName = "test1";
        when(counterRepository.findByName(existentName)).thenReturn(Mono.just(new Counter(1l, existentName, 10l)));

        StepVerifier.create(counterService.create(existentName))
                .expectError(CounterAlreadyExistsException.class)
                .verify();
    }

    @Test
    public void shouldFailWhenTryGetNonexistentCounter() {
        String name = "nonexistentCounter";

        when(counterRepository.findByName(name)).thenReturn(Mono.empty());

        StepVerifier.create(counterService.findByName(name))
                .expectError(CounterNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldFailWhenTryIncrementNonexistentCounter() {
        String name = "nonexistentCounter";

        when(counterRepository.findByName(name)).thenReturn(Mono.empty());
        StepVerifier.create(counterService.increment(name))
                .expectError(CounterNotFoundException.class)
                .verify();
    }

    @Test
    public void shouldIncrementCounter() {
        String name = "test1";

        Counter counter = new Counter(1l, name, 0l);
        Counter expectedCounter = new Counter(1l, name, 1l);;
        when(counterRepository.findByName(name)).thenReturn(Mono.just(counter));
        when(counterRepository.save(expectedCounter)).thenReturn(Mono.just(expectedCounter));
        StepVerifier.create(counterService.increment(name))
                .expectNextMatches(savedCounter -> {
                    assertThat(savedCounter).isEqualTo(expectedCounter);
                    return true;
                })
                .expectComplete()
                .verify();
    }
}