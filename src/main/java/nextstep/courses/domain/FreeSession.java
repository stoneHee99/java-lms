package nextstep.courses.domain;

import java.io.File;
import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(String title, File imageFile, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, new CoverImage(imageFile), startDate, endDate);
    }
}
