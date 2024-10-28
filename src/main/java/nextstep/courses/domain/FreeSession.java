package nextstep.courses.domain;

import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(String title, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, startDate, endDate);
    }
}
