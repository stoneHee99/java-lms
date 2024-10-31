package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public abstract class Session {

    private final Long id;

    private final String title;

    private final SessionType sessionType;

    private SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    private final Set<NsUser> enrolledUsers = new HashSet<>();

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    protected boolean isEnrollmentOpen() {
        return SessionStatus.isEnrollmentOpen(status);
    }

    protected int getEnrolledUserCount() {
        return enrolledUsers.size();
    }

    public void startRecruitment() {
        if (!status.nextState().equals(SessionStatus.RECRUITING)) {
            throw new IllegalStateException("모집을 시작할 수 없는 상태입니다.");
        }
        this.status = status.nextState();
    }

    Session(Long id, String title, SessionType sessionType, CoverImage image, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.sessionType = sessionType;
        this.coverImage = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    protected void enrollUser(NsUser user) {
        enrolledUsers.add(user);
    }

    public SessionType getSessionType() {
        return sessionType;
    }
}
