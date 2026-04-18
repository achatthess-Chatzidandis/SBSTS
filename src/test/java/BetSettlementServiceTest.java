
import com.chatzidandis.repository.BetRepository;
import com.chatzidandis.enums.BetStatus;
import com.chatzidandis.model.BetEntity;
import com.chatzidandis.model.EventOutcome;
import com.chatzidandis.rocketmq.producer.BetSettlementProducer;
import com.chatzidandis.service.BetSettlementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BetSettlementServiceTest {

    private BetRepository betRepository;
    private BetSettlementProducer producer;
    private BetSettlementService service;

    @BeforeEach
    void setup() {
        betRepository = mock(BetRepository.class);
        producer = mock(BetSettlementProducer.class);
        service = new BetSettlementService(betRepository, producer);
    }

    @Test
    void shouldPrepareAndSendSettlements_correctly() {
        // GIVEN
        EventOutcome event = new EventOutcome(100L, "Match A", 1001L);

        BetEntity bet1 = new BetEntity(1L, 10L, 100L, 1000L, 1001L, 100D, BetStatus.PENDING); // WON
        BetEntity bet2 = new BetEntity(2L, 20L, 100L, 1000L, 9999L, 50D, BetStatus.PENDING);  // LOST

        when(betRepository.findBetsForSettlementByEventId(100L))
                        .thenReturn(List.of(bet1, bet2));

        // WHEN
        List<Long> result = service.prepareAndSendSettlements(event);

        // THEN
        assertThat(result).containsExactlyInAnyOrder(1L, 2L);

        ArgumentCaptor<com.chatzidandis.model.BetSettlement> captor =
                        ArgumentCaptor.forClass(com.chatzidandis.model.BetSettlement.class);

        verify(producer, times(2)).send(captor.capture());

        List<com.chatzidandis.model.BetSettlement> sent = captor.getAllValues();

        assertThat(sent).anyMatch(s ->
                        s.getBetId().equals(1L) &&
                                        s.getStatus().equals("WON") &&
                                        s.getAmount().equals(100D)
        );

        assertThat(sent).anyMatch(s ->
                        s.getBetId().equals(2L) &&
                                        s.getStatus().equals("LOST") &&
                                        s.getAmount().equals(50D)
        );
    }
}