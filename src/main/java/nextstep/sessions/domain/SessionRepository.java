package nextstep.sessions.domain;

import java.util.Optional;

public interface SessionRepository {

    Optional<Session> findById(Long id);
}
