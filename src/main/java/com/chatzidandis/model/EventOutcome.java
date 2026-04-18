package com.chatzidandis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventOutcome {
    private Long eventId;
    private String eventName;
    private Long winnerId;
}
