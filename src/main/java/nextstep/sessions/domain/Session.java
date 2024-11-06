package nextstep.sessions.domain;

import nextstep.qna.UnAuthorizedException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Session {

    private final Long id;

    private final Long courseId;

    private final String title;

    private final SessionType sessionType;

    private SessionProgressStatus progressStatus = SessionProgressStatus.PREPARING;

    private RecruitmentStatus recruitmentStatus = RecruitmentStatus.CLOSED;

    private final List<CoverImage> coverImages = new ArrayList<>();

    private final Set<Enrollment> enrollments = new HashSet<>();

    private final DateRange sessionDateRange;

    protected boolean isEnrollmentOpen() {
        return recruitmentStatus == RecruitmentStatus.OPEN;
    }

    public void startRecruitment() {
        this.recruitmentStatus = RecruitmentStatus.OPEN;
    }

    public Session(Long id, Long courseId, String title, SessionType sessionType, List<CoverImage> coverImages, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.sessionType = sessionType;
        this.sessionDateRange = new DateRange(startDate, endDate);
        setCoverImages(coverImages);
    }

    public Session(Long id, Long courseId, String title, SessionType sessionType, CoverImage coverImage, LocalDateTime startDate, LocalDateTime endDate) {
        this(id, courseId, title, sessionType, Collections.singletonList(coverImage), startDate, endDate);
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

    public SessionProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public RecruitmentStatus getRecruitmentStatus() {
        return recruitmentStatus;
    }

    @Deprecated
    public CoverImage getCoverImage() {
        return coverImages.iterator().next();
    }

    public List<CoverImage> getCoverImages() {
        return Collections.unmodifiableList(coverImages);
    }

    public DateRange getSessionDateRange() {
        return sessionDateRange;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setCoverImages(List<CoverImage> coverImages) {
        this.coverImages.clear();
        this.coverImages.addAll(coverImages);
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments.clear();
        this.enrollments.addAll(enrollments);
    }

    protected int getEnrolledUserCount() {
        return (int) enrollments.stream()
                .filter(Enrollment::isApproved)
                .count();
    }

    protected int getPendingUserCount() {
        return (int) enrollments.stream()
                .filter(Enrollment::isPending)
                .count();
    }

    public void approveEnrollment(NsUser instructor, Long userId) {
        validateInstructor(instructor);
        Enrollment enrollment = findPendingEnrollment(userId);
        enrollment.approve();
    }

    public void rejectEnrollment(NsUser instructor, Long userId) {
        validateInstructor(instructor);
        Enrollment enrollment = findPendingEnrollment(userId);
        enrollment.reject();
    }

    private Enrollment findPendingEnrollment(Long userId) {
        return enrollments.stream()
                .filter(e -> e.getUser().getId().equals(userId))
                .filter(Enrollment::isPending)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("대기중인 수강신청을 찾을 수 없습니다."));
    }

    private void validateInstructor(NsUser instructor) {
        // TODO: 강사 권한 검증 로직 구현
        if (!isInstructor(instructor)) {
            throw new UnAuthorizedException("강사만 수강 승인/거절이 가능합니다.");
        }
    }

    private boolean isInstructor(NsUser user) {
        // TODO: 강사 여부 확인 로직 구현
        return true;
    }

    public List<Enrollment> getPendingEnrollments() {
        return enrollments.stream()
                .filter(Enrollment::isPending)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Enrollment> getApprovedEnrollments() {
        return enrollments.stream()
                .filter(Enrollment::isApproved)
                .collect(Collectors.toUnmodifiableList());
    }
}
