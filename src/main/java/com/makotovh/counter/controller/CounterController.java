package com.makotovh.counter.controller;

import com.makotovh.counter.model.Counter;
import com.makotovh.counter.model.CreateCounterRequest;
import com.makotovh.counter.service.CounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/counter")
@Value
public class CounterController {

    CounterService counterService;

    @Operation(summary = "Create a Counter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Counter created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid name supplied",
                    content = @Content)
            })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Counter> createCounter(@RequestBody @Valid CreateCounterRequest request) {
        return counterService.create(request.getName());
    }

    @Operation(summary = "List all Counters")
    @ApiResponse(responseCode = "200", description = "Found the Counter",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)) })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Counter> list() {
        return counterService.findAll();
    }

    @Operation(summary = "Get a Counter by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Counter",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "404", description = "Counter not found",
                    content = @Content) })
    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Long> get(@PathVariable String name) {
        return counterService.findByName(name).map(Counter::getQuantity);
    }

    @Operation(summary = "Increment a Counter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Counter was incremented",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "404", description = "Counter not found",
                    content = @Content) })
    @PostMapping(value = "/{name}/increment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Counter> increment(@PathVariable String name) {
        return counterService.increment(name);
    }
}
