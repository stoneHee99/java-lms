package nextstep.sessions.infrastructure;

import nextstep.sessions.domain.Session;
import nextstep.sessions.domain.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("sessionRepository")
public class JdbcSessionRepository implements SessionRepository {

    @Override
    public Optional<Session> findById(Long id) {
        return Optional.empty();
    }
}
