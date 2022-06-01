package romanow.abc.bridge.constants;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExamPeriodState {
    REDACTION(0),
    ALLOWANCE(1),
    READY(2),
    PROGRESS(3),
    FINISHED(4),
    CLOSED(5);

    private final int order;

    public boolean isAllowed(ExamPeriodState state) {
        return state.order + 1 == this.order;
    }

    public boolean isAfter(ExamPeriodState state) {
        return this.order > state.order;
    }

}
