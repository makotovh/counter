package com.makotovh.counter.controller;

import com.makotovh.counter.config.SecurityConfig;
import com.makotovh.counter.model.Counter;
import com.makotovh.counter.model.CreateCounterRequest;
import com.makotovh.counter.service.CounterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@WebFluxTest(value = CounterController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class CounterControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CounterService counterService;

    @Value("${spring.security.user.name}")
    private String userName;

    @Value("${spring.security.user.password}")
    private String password;

    @Test
    public void createCounter() {
        String name = "counter1";
        Counter expectedCounter = new Counter(null ,name, 0l);

        when(counterService.create(name)).thenReturn(Mono.just(expectedCounter));

        webTestClient.post()
                .uri("/api/v1/counter")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .bodyValue(new CreateCounterRequest(name))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Counter.class)
                .isEqualTo(expectedCounter);

    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "     "} )
    public void shouldFailWhenTryCreateCounterWithInvalidName(String name) {
        Counter expectedCounter = new Counter(null ,name, 0l);
        when(counterService.create(name)).thenReturn(Mono.just(expectedCounter));
        webTestClient.post()
                .uri("/api/v1/counter")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .bodyValue(new CreateCounterRequest(name))
                .exchange()
                .expectStatus().is4xxClientError();

    }

    @Test
    public void list() {
        Counter firstCounter = new Counter(null ,"counter1", 0l);
        Counter secondCounter = new Counter(null ,"counter2", 10l);
        Flux<Counter> expectedList = Flux.just(firstCounter, secondCounter);
        when(counterService.findAll()).thenReturn(expectedList);

        webTestClient.get()
                .uri("/api/v1/counter")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Counter.class)
                .value(List::size, equalTo(2))
                .value(list -> list.get(0), equalTo(firstCounter))
                .value(list -> list.get(1), equalTo(secondCounter));
    }

    @Test
    public void getCounter() {
        String name = "counter1";
        Counter firstCounter = new Counter(null ,name, 10l);

        when(counterService.findByName(name)).thenReturn(Mono.just(firstCounter));

        webTestClient.get()
                .uri("/api/v1/counter/counter1")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .isEqualTo(10l);
    }

    @Test
    public void incrementCounter() {
        String name = "counter1";

        Counter counter1 = new Counter(1l ,name, 10l);
        Counter incrementedCounter = new Counter(1l ,name, 11l);

        when(counterService.increment(name)).thenReturn(Mono.just(incrementedCounter));

        webTestClient.post()
                .uri("/api/v1/counter/counter1/increment")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Counter.class)
                .isEqualTo(incrementedCounter);
    }
}