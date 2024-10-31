package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final long price;
    private final int maxParticipants;

    public PaidSession(String title, long price, int maxParticipants, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, SessionType.PAID, coverImage, startDate, endDate);
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll(payment)) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
        enrollUser(user);
    }

    private boolean canEnroll(Payment payment) {
        return (isEnrollmentOpen() && payment.isSamePrice(price) && maxParticipants > getEnrolledUserCount());
    }
}
