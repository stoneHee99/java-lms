package nextstep.sessions.infrastructure;

import nextstep.sessions.domain.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository("sessionRepository")
public class JdbcSessionRepository implements SessionRepository {
    private final JdbcOperations jdbcTemplate;

    public JdbcSessionRepository(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Session> findById(Long id) {
        String sql = "SELECT s.id, s.course_id, s.title, s.session_type, s.status, " +
                "s.start_date, s.end_date, " +
                "c.id as cover_id, c.file_size, c.file_extension, c.width, c.height, " +
                "p.price, p.max_participants " +
                "FROM session s " +
                "INNER JOIN cover_image c ON s.id = c.session_id " +
                "LEFT JOIN paid_session_info p ON s.id = p.session_id " +
                "WHERE s.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new SessionRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class SessionRowMapper implements RowMapper<Session> {
        @Override
        public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
            SessionType sessionType = SessionType.valueOf(rs.getString("session_type"));

            CoverImage coverImage = createCoverImage(rs);
            LocalDateTime startDate = toLocalDateTime(rs.getTimestamp("start_date"));
            LocalDateTime endDate = toLocalDateTime(rs.getTimestamp("end_date"));

            if (sessionType == SessionType.FREE) {
                return new FreeSession(
                        rs.getLong("id"),
                        rs.getLong("course_id"),
                        rs.getString("title"),
                        coverImage,
                        startDate,
                        endDate
                );
            }

            return new PaidSession(
                    rs.getLong("id"),
                    rs.getLong("course_id"),
                    rs.getString("title"),
                    rs.getLong("price"),
                    rs.getInt("max_participants"),
                    coverImage,
                    startDate,
                    endDate
            );
        }

        private CoverImage createCoverImage(ResultSet rs) throws SQLException {
            return new CoverImage(
                    rs.getInt("file_size"),
                    rs.getString("file_extension"),
                    rs.getInt("width"),
                    rs.getInt("height")
            );
        }

        private LocalDateTime toLocalDateTime(Timestamp timestamp) {
            if (timestamp == null) {
                return null;
            }
            return timestamp.toLocalDateTime();
        }
    }
}
