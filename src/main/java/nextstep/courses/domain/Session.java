package nextstep.courses.domain;

import java.time.LocalDateTime;

public abstract class Session {

    private final Long id;

    private final String title;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    protected Session(Long id, String title, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
