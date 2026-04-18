
import com.chatzidandis.repository.BetRepository;
import com.chatzidandis.enums.BetStatus;
import com.chatzidandis.model.BetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BetRepositoryTest {

    private BetRepository repository;

    @BeforeEach
    void setup() {
        repository = new BetRepository();
        repository.init();
    }

    @Test
    void shouldReturnOnlyPendingBetsForEvent() {
        List<BetEntity> bets = repository.findBetsForSettlementByEventId(100L);

        assertThat(bets).hasSize(1);
        assertThat(bets.get(0).getEventId()).isEqualTo(100L);
        assertThat(bets.get(0).getStatus()).isEqualTo(BetStatus.PENDING);
    }

    @Test
    void shouldSettleBet() {
        repository.settleBet(1L);

        List<BetEntity> bets = repository.findBetsForSettlementByEventId(100L);

        assertThat(bets).isEmpty(); // no longer pending
    }

    @Test
    void shouldThrowIfBetNotFound() {
        assertThatThrownBy(() -> repository.settleBet(999L))
                        .isInstanceOf(IllegalArgumentException.class);
    }
}