package com.chatzidandis.kafka.producer;

import com.chatzidandis.model.EventOutcome;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "event-outcomes";

    public void publish(EventOutcome event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            String key = String.valueOf(event.getEventId());
            log.info("Publishing EventOutcome to topic {} with key {}: {}", TOPIC, key, message);
            kafkaTemplate.send(
                            TOPIC,
                            String.valueOf(event.getEventId()),
                            message
            );

        } catch (Exception e) {
            log.error("Failed to serialize EventOutcome: {}", event, e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
