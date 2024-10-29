package nextstep.courses.infrastructure;

import nextstep.courses.domain.Session;
import nextstep.courses.domain.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("sessionRepository")
public class MockSessionRepository implements SessionRepository {

    @Override
    public Optional<Session> findById(Long id) {
        return Optional.empty();
    }
}
