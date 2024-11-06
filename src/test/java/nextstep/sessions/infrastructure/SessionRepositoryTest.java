package nextstep.sessions.infrastructure;

import nextstep.sessions.domain.*;
import nextstep.users.domain.UserRepository;
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
        CoverImage coverImage = new CoverImage(512000, "JPG", 300, 200);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(30);

        Session session = new FreeSession(
                0L,
                1L,
                "TDD 강의",
                coverImage,
                startDate,
                endDate
        );

        int saveResult = sessionRepository.save(session);
        Optional<Session> foundSession = sessionRepository.findById(1L);

        assertThat(saveResult).isEqualTo(1);
        assertThat(foundSession).isPresent();

        Session actual = foundSession.get();
        assertAll(
                () -> assertThat(actual.getSessionType()).isEqualTo(SessionType.FREE),
                () -> assertThat(actual).isInstanceOf(FreeSession.class),
                () -> assertThat(actual.getTitle()).isEqualTo("TDD 강의"),
                () -> assertThat(actual.getCourseId()).isEqualTo(1L)
        );
    }
}
