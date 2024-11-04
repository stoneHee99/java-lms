package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

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
}
