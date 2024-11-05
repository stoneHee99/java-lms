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
    public int save(Session session) {
        String sql = "INSERT INTO session (course_id, title, session_type, status, start_date, end_date, price, max_participants) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        DateRange dateRange = session.getSessionDateRange();

        int result = jdbcTemplate.update(sql,
                session.getCourseId(),
                session.getTitle(),
                session.getSessionType().name(),
                session.getStatus(),
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                session instanceof PaidSession ? ((PaidSession) session).getPrice() : null,
                session instanceof PaidSession ? ((PaidSession) session).getMaxParticipants() : null
        );

        if (result > 0) {
            String coverImageSql = "INSERT INTO cover_image (session_id, file_size, file_extension, width, height) " +
                    "VALUES (?, ?, ?, ?, ?)";

            CoverImage coverImage = session.getCoverImage();
            CoverImageDimension coverImageDimension = coverImage.getDimension();
            jdbcTemplate.update(coverImageSql,
                    session.getId(),
                    coverImage.getFileSize(),
                    coverImage.getFileExtension(),
                    coverImageDimension.getWidth(),
                    coverImageDimension.getHeight()
            );
        }

        return result;
    }

    @Override
    public Optional<Session> findById(Long id) {
        String sql = "SELECT s.id, s.course_id, s.title, s.session_type, s.status, " +
                "s.start_date, s.end_date, s.price, s.max_participants, " +
                "c.id as cover_id, c.file_size, c.file_extension, c.width, c.height " +
                "FROM session s " +
                "INNER JOIN cover_image c ON s.id = c.session_id " +
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
