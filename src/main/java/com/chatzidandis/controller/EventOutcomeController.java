package com.chatzidandis.controller;

import com.chatzidandis.kafka.producer.EventOutcomeProducer;
import com.chatzidandis.model.EventOutcome;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventOutcomeController {

    private final EventOutcomeProducer producer;

    @PostMapping("/outcome")
    public ResponseEntity<String> publish(@RequestBody EventOutcome outcome) {
        producer.publish(outcome);
        return ResponseEntity.ok("Published");
    }
}
