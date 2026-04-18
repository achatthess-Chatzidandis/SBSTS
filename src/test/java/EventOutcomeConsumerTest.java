package com.chatzidandis.kafka.consumer;

import com.chatzidandis.model.EventOutcome;
import com.chatzidandis.service.BetSettlementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class EventOutcomeConsumerTest {

    @Test
    void shouldConsumeAndTriggerSettlement() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        BetSettlementService service = mock(BetSettlementService.class);

        EventOutcomeConsumer consumer =
                        new EventOutcomeConsumer(objectMapper, service);

        EventOutcome event = new EventOutcome(100L, "Match A", 1001L);

        String json = objectMapper.writeValueAsString(event);

        when(service.prepareAndSendSettlements(any()))
                        .thenReturn(List.of(1L, 2L));

        // WHEN
        consumer.consume(json);

        // THEN
        verify(service).prepareAndSendSettlements(any());
        verify(service).settleBet(1L);
        verify(service).settleBet(2L);
    }
}