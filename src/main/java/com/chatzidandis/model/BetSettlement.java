package com.chatzidandis.model;

import lombok.Data;

@Data
public class BetSettlement {
    private Long betId;
    private String status; // WON / LOST
    private Double amount;
}
