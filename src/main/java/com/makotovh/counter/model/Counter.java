package com.makotovh.counter.model;

import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
public class Counter {
    @Id
    Long id;
    String name;
    Long quantity;

    public Counter increment() {
        return new Counter(id, name, quantity + 1);
    }
}
