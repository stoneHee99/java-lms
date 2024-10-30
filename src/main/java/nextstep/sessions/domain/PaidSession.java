package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.io.File;
import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final int maxParticipants;

    public PaidSession(String title, long price, int maxParticipants, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, price, imageFile, startDate, endDate);
        this.maxParticipants = maxParticipants;
    }

    @Override
    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll(payment)) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
        enrollUser(user);
    }

    private boolean canEnroll(Payment payment) {
        return (isEnrollmentOpen() && payment.isSamePrice(getPrice()) && maxParticipants > getEnrolledUserCount());
    }
}
