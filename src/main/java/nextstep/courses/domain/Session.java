package nextstep.courses.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public abstract class Session {

    private final Long id;

    private final String title;

    private SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    protected final Set<NsUser> enrolledUsers = new HashSet<>();

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public abstract void enroll(Payment payment, NsUser user);

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

    protected Session(Long id, String title, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.coverImage = new CoverImage(imageFile);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
