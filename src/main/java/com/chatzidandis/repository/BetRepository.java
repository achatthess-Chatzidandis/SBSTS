package com.chatzidandis.repository;

import com.chatzidandis.enums.BetStatus;
import com.chatzidandis.model.BetEntity;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BetRepository {

    private final Map<Long, BetEntity> storage = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        storage.put(1L, new BetEntity(1L,10L,100L,1000L,1001L,100D,BetStatus.PENDING));
        storage.put(2L, new BetEntity(2L,20L,200L,2000L,2001L,150D,BetStatus.PENDING));
    }

    public List<BetEntity> findBetsForSettlementByEventId(Long eventId) {
        return storage.values().stream()
                        .filter(b -> b.getEventId().equals(eventId) && b.getStatus().equals(BetStatus.PENDING))
                        .toList();
    }

    public void settleBet(long betId) {
        BetEntity bet = storage.get(betId);

        if (bet == null) {
            throw new IllegalArgumentException("Bet not found: " + betId);
        }

        if (bet.getStatus() == BetStatus.SETTLED) {
            throw new IllegalStateException("Bet already settled: " + betId);
        }

        bet.setStatus(BetStatus.SETTLED);
    }
}
