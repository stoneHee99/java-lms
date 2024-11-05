package nextstep.sessions.domain;

import java.util.Optional;

public interface SessionRepository {
    int save(Session session);

    Optional<Session> findById(Long id);
}
