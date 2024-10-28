package nextstep.courses.domain;

import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final int price;

    private final int maxParticipants;

    public PaidSession(String title, int price, int maxParticipants, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, startDate, endDate);
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

}
