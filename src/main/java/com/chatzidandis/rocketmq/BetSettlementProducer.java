package com.chatzidandis.rocketmq;

import com.chatzidandis.model.BetSettlement;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetSettlementProducer {

    private final RocketMQTemplate rocketMQTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "bet-settlements";

    public void send(BetSettlement settlement) {
        try {
            String json = objectMapper.writeValueAsString(settlement);
            log.info("Sending BetSettlement to topic {}: {}", TOPIC, json);
            rocketMQTemplate.convertAndSend(TOPIC, json);
        } catch (Exception e) {
            log.error("Failed to send BetSettlement: {}", settlement, e);
            throw new RuntimeException(e);
        }
    }
}
