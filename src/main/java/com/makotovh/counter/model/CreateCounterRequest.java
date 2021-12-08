package com.makotovh.counter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CreateCounterRequest {

    @NotBlank
    String name;

    @JsonCreator
    public CreateCounterRequest(@JsonProperty String name) {
        this.name = name;
    }
}
