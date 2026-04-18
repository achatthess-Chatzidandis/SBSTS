package com.chatzidandis.model;

import com.chatzidandis.enums.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetEntity {
    private Long betId;
    private Long userId;
    private Long eventId;
    private Long marketId;
    private Long winnerId;
    private Double betAmount;

    private BetStatus status;
}
