package nextstep.sessions.domain;

public enum EnrollmentStatus {
    CLOSED("비모집중"),
    OPEN("모집중");

    private final String description;

    EnrollmentStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
