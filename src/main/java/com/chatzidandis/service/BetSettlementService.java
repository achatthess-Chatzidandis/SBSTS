package com.chatzidandis.service;

import com.chatzidandis.model.BetEntity;
import com.chatzidandis.model.BetSettlement;
import com.chatzidandis.model.EventOutcome;
import com.chatzidandis.repository.BetRepository;
import com.chatzidandis.rocketmq.producer.BetSettlementProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BetSettlementService {

    private final BetRepository betRepository;
    private final BetSettlementProducer producer;

    public List<Long> prepareAndSendSettlements(EventOutcome event) {

        List<Long> betIdsToSettle = new ArrayList<>();
        List<BetEntity> bets = betRepository.findBetsForSettlementByEventId(event.getEventId());

        for (BetEntity bet : bets) {

            boolean won = bet.getWinnerId()
                            .equals(event.getWinnerId());

            BetSettlement settlement = new BetSettlement();
            settlement.setBetId(bet.getBetId());
            settlement.setStatus(won ? "WON" : "LOST");
            settlement.setAmount(bet.getBetAmount());

            betIdsToSettle.add(bet.getBetId());

            producer.send(settlement);
        }
        return betIdsToSettle;
    }

    public void settleBet(long betId) {
        betRepository.settleBet(betId);
    }
}
