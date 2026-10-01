package sumamounts;

import java.util.List;

public final class SumAmounts {
    private SumAmounts() {}

    public record Obligation(long id, long amountDue, long priority) {}

    public record Line(long id, long applied) {}

    public record Settlement(List<Line> lines, long unapplied) {}

    public static Settlement settle(long payment, List<Obligation> obligations) {
        throw new UnsupportedOperationException("not implemented");
    }
}
