package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.Objects;

public class Enrollment {

    private final Long id;
    private final Long sessionId;
    private final NsUser user;
    private final LocalDateTime enrolledAt;

    public Enrollment(Long id, Long sessionId, NsUser user, LocalDateTime enrolledAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.user = user;
        this.enrolledAt = enrolledAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(id, that.id) && Objects.equals(sessionId, that.sessionId) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, user);
    }
}
