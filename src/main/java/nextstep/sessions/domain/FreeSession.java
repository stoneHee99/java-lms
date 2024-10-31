package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(String title, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(0L, title, SessionType.FREE, coverImage, startDate, endDate);
    }

    public void enroll(NsUser user) {
        if (!canEnroll()) {
            throw new UnsupportedOperationException("수강 신청이 불가능한 상태입니다");
        }
        enrollUser(user);
    }

    private boolean canEnroll() {
        return (isEnrollmentOpen());
    }
}
