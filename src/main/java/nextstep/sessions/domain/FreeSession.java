package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class FreeSession extends Session {

    public FreeSession(Long id, Long courseId, String title, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(id, courseId, title, SessionType.FREE, coverImage, startDate, endDate);
    }

    public void enroll(NsUser user) {
        if (!canEnroll()) {
            throw new IllegalStateException("수강 신청이 불가능한 상태입니다");
        }
        enroll(new Enrollment(0L, this.getId(), user, LocalDateTime.now()));
    }

    private boolean canEnroll() {
        return (isEnrollmentOpen());
    }
}
