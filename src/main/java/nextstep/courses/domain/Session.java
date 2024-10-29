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

    private final long price;

    private SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    private final Set<NsUser> enrolledUsers = new HashSet<>();

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    public abstract void enroll(Payment payment, NsUser user);

    protected long getPrice() {
        return price;
    }

    protected boolean isEnrollmentOpen() {
        return SessionStatus.isEnrollmentOpen(status);
    }

    protected int getEnrolledUserCount() {
        return enrolledUsers.size();
    }

    public void startRecruitment() {
        if (!status.nextState().equals(SessionStatus.RECRUITING)) {
            throw new IllegalStateException("모집을 시작할 수 없는 상태입니다.");
        }
        this.status = status.nextState();
    }

    private Session(Long id, String title, long price, CoverImage image, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.coverImage = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Session(Long id, String title, long price, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        this(id, title, price, new CoverImage(imageFile), startDate, endDate);
    }

    protected void enrollUser(NsUser user) {
        enrolledUsers.add(user);
    }
}
