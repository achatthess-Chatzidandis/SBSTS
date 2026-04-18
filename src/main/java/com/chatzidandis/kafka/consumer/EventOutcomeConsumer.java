package com.chatzidandis.kafka.consumer;

import com.chatzidandis.model.EventOutcome;
import com.chatzidandis.service.BetSettlementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j @Service
@RequiredArgsConstructor
public class EventOutcomeConsumer {

    private final ObjectMapper objectMapper;
    private final BetSettlementService betSettlementService;

    @KafkaListener(topics = "event-outcomes", groupId = "betting-group")
    public void consume(String message) {

        try {
            EventOutcome event =
                            objectMapper.readValue(message, EventOutcome.class);

            List<Long> betIdsToSettle = betSettlementService.prepareAndSendSettlements(event);
            betIdsToSettle.forEach(betSettlementService::settleBet);

        } catch (Exception e) {
            log.info("Failed to process event: " + message);
            e.printStackTrace();
        }
    }
}
