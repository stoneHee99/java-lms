package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final long price;
    private final int maxParticipants;

    public PaidSession(Long id, Long courseId, String title, long price, int maxParticipants, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(id, courseId, title, SessionType.PAID, coverImage, startDate, endDate);
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll(payment)) {
            throw new IllegalStateException("수강 신청이 불가능한 상태입니다");
        }
        enroll(new Enrollment(0L, this.getId(), user, LocalDateTime.now()));
    }

    private boolean canEnroll(Payment payment) {
        return (isEnrollmentOpen() && payment.isSamePrice(price) && maxParticipants > getEnrolledUserCount());
    }

    public long getPrice() {
        return price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }
}
