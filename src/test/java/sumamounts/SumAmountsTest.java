package sumamounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import sumamounts.SumAmounts.Line;
import sumamounts.SumAmounts.Obligation;
import sumamounts.SumAmounts.Settlement;

class SumAmountsTest {
    private static Obligation ob(long id, long amountDue, long priority) {
        return new Obligation(id, amountDue, priority);
    }

    private static void assertSettlement(long payment, List<Obligation> obligations, long unapplied, Line... lines) {
        assertEquals(new Settlement(List.of(lines), unapplied), SumAmounts.settle(payment, obligations));
    }

    @Test
    void empty() {
        assertSettlement(100, List.of(), 100);
    }

    @Test
    void paysInFull() {
        assertSettlement(
                500,
                List.of(ob(1, 100, 1), ob(2, 40, 3), ob(3, 60, 2)),
                300,
                new Line(1, 100),
                new Line(2, 40),
                new Line(3, 60));
    }

    @Test
    void higherPriorityIsPaidFirst() {
        assertSettlement(
                60,
                List.of(ob(1, 80, 1), ob(2, 50, 9), ob(3, 25, 9)),
                0,
                new Line(1, 0),
                new Line(2, 50),
                new Line(3, 10));
    }

    @Test
    void samePriorityPaysEarlierInputFirst() {
        assertSettlement(
                10,
                List.of(ob(1, 50, 1), ob(2, 30, 1), ob(3, 20, 1)),
                0,
                new Line(1, 10),
                new Line(2, 0),
                new Line(3, 0));
    }

    @Test
    void remainderSpillsToTheNextPriority() {
        assertSettlement(
                120,
                List.of(ob(1, 100, 1), ob(2, 40, 5), ob(3, 60, 5)),
                0,
                new Line(1, 20),
                new Line(2, 40),
                new Line(3, 60));
    }

    @Test
    void duplicateId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SumAmounts.settle(5, List.of(ob(1, 10, 1), ob(1, 4, 2))));
    }
}
