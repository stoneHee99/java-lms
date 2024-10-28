package nextstep.courses.domain;

import nextstep.payments.domain.Payment;

import java.time.LocalDateTime;

public abstract class Session {

    private final Long id;

    private final String title;

    private final SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public abstract void enroll(Payment payment);

    protected boolean canEnroll() {
        return SessionStatus.CanEnroll(status);
    }

    protected Session(Long id, String title, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
