package nextstep.courses.domain;

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

}
