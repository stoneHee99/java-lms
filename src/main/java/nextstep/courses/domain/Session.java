package nextstep.courses.domain;

import nextstep.payments.domain.Payment;

import java.time.LocalDateTime;

public abstract class Session {

    private final Long id;

    private final String title;

    private SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public abstract void enroll(Payment payment);

    protected boolean canEnroll() {
        return SessionStatus.CanEnroll(status);
    }

    public void startRecruitment() {
        if (!status.nextState()
                .equals(SessionStatus.RECRUITING)) {
            throw new IllegalStateException("모집을 시작할 수 없는 상태입니다.");
        }
        status = status.nextState();
    }

    protected Session(Long id, String title, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
