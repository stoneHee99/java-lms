package nextstep.courses.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.io.File;
import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final long price;

    private final int maxParticipants;

    public PaidSession(String title, int price, int maxParticipants, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, imageFile, startDate, endDate);
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    @Override
    public void enroll(Payment payment, NsUser user) {
        if (!canEnroll() || !payment.isSamePrice(price)) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
    }
}
