package nextstep.courses.domain;

import java.time.LocalDateTime;

public abstract class Session {

    private final Long id;

    private final String name;

    private final LocalDateTime createDate;

    private final LocalDateTime endDate;

    protected Session(Long id, String name, LocalDateTime createDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.endDate = endDate;
    }
}
