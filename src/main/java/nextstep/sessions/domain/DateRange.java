package nextstep.sessions.domain;

import java.time.LocalDateTime;

public class DateRange {

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜 이전이어야 합니다.");
        }
    }
}
