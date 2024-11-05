package nextstep.sessions.infrastructure;

import nextstep.sessions.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class SessionRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRepositoryTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository = new JdbcSessionRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO course (id, title, creator_id, created_at)" +
                        "VALUES (1, 'Test Course', 1, ?)",
                LocalDateTime.now());
    }

    @Test
    void crud() {
        jdbcTemplate.update("INSERT INTO session (id, course_id, title, session_type, status, start_date, end_date)" +
                        "VALUES (1, 1, 'TDD 강의', 'FREE', 'PREPARING', ?, ?)",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30));

        jdbcTemplate.update("INSERT INTO cover_image (session_id, file_size, file_extension, width, height)" +
                "VALUES (1, 512000, 'JPG', 300, 200)");

        Optional<Session> session = sessionRepository.findById(1L);

        assertThat(session).isPresent();
        Session foundSession = session.get();

        assertAll(
                () -> assertThat(foundSession.getSessionType()).isEqualTo(SessionType.FREE),
                () -> assertThat(foundSession).isInstanceOf(FreeSession.class)
        );
        LOGGER.debug("Session: {}", foundSession);
    }
}
