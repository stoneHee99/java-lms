package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class Enrollment {

    private final Long id;
    private final Session session;
    private final NsUser user;
    private final LocalDateTime enrolledAt;

    public Enrollment(Long id, Session session, NsUser user, LocalDateTime enrolledAt) {
        this.id = id;
        this.session = session;
        this.user = user;
        this.enrolledAt = enrolledAt;
    }
}
