package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public abstract class Session {

    private final Long id;

    private final Long courseId;

    private final String title;

    private final SessionType sessionType;

    private SessionStatus status = SessionStatus.PREPARING;

    private final CoverImage coverImage;

    private final Set<Enrollment> enrollments = new HashSet<>();

    private final DateRange sessionDateRange;

    protected boolean isEnrollmentOpen() {
        return SessionStatus.isEnrollmentOpen(status);
    }

    protected int getEnrolledUserCount() {
        return enrollments.size();
    }

    public void startRecruitment() {
        if (!status.nextState().equals(SessionStatus.RECRUITING)) {
            throw new IllegalStateException("모집을 시작할 수 없는 상태입니다.");
        }
        this.status = status.nextState();
    }

    Session(Long id, Long courseId, String title, SessionType sessionType, CoverImage image, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.sessionType = sessionType;
        this.coverImage = image;
        this.sessionDateRange = new DateRange(startDate, endDate);
    }


    protected void enroll(Enrollment enrollment) {
        int currentEnrolledUserCount = enrollments.size();
        enrollments.add(enrollment);
        if (currentEnrolledUserCount == enrollments.size()) {
            throw new IllegalArgumentException("이미 수강 신청한 사용자입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status.toString();
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public DateRange getSessionDateRange() {
        return sessionDateRange;
    }

    public SessionType getSessionType() {
        return sessionType;
    }
}
