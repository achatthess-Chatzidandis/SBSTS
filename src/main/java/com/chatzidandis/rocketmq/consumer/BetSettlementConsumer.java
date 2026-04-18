package com.chatzidandis.rocketmq.consumer;

import com.chatzidandis.model.BetSettlement;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
                topic = "bet-settlements",
                consumerGroup = "bet-settlement-consumer-group"
)
public class BetSettlementConsumer implements RocketMQListener<String> {

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(String message) {
        try {
            BetSettlement settlement =
                            objectMapper.readValue(message, BetSettlement.class);

            log.info("Received settlement: {}", settlement);

            // Simulate downstream processing
            processSettlement(settlement);

        } catch (Exception e) {
            log.error("Failed to process settlement message: {}", message, e);
        }
    }

    private void processSettlement(BetSettlement settlement) {
        // Simulate business logic
        if ("WON".equals(settlement.getStatus())) {
            log.info("Paying user for bet {} amount {}",
                            settlement.getBetId(),
                            settlement.getAmount());
        } else {
            log.info("Bet {} lost. No payout.", settlement.getBetId());
        }
    }
}