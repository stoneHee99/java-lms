package nextstep.courses.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.io.File;
import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(String title, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, imageFile, startDate, endDate);
    }

    @Override
    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll() || !payment.isFree()) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
        enrolledUsers.add(user);
    }
}
