package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(String title, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, 0, coverImage, startDate, endDate);
    }

    @Override
    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll(payment)) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
        enrollUser(user);
    }

    private boolean canEnroll(Payment payment) {
        return (isEnrollmentOpen() && payment.isFree());
    }
}
