package nextstep.courses.domain;

import nextstep.payments.domain.Payment;

import java.io.File;
import java.time.LocalDateTime;

public class PaidSession extends Session {

    private final int price;

    private final int maxParticipants;

    public PaidSession(String title, int price, int maxParticipants, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, new CoverImage(imageFile), startDate, endDate);
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    @Override
    public void enroll(Payment payment) {
        if (!canEnroll()) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
    }
}
