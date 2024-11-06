package nextstep.sessions.infrastructure;

import nextstep.sessions.domain.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int sessionResult = saveSession(session, keyHolder);
        saveCoverImage(session.getCoverImage(), keyHolder.getKey().longValue());
        return sessionResult;
    }

    private int saveSession(Session session, KeyHolder keyHolder) {
        String sql = "INSERT INTO session (course_id, title, session_type, status, start_date, end_date, price, max_participants) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setSessionParameters(ps, session);
            return ps;
        }, keyHolder);
    }

    private void setSessionParameters(PreparedStatement ps, Session session) throws SQLException {
        DateRange dateRange = session.getSessionDateRange();
        ps.setLong(1, session.getCourseId());
        ps.setString(2, session.getTitle());
        ps.setString(3, session.getSessionType().name());
        ps.setString(4, session.getStatus());
        ps.setTimestamp(5, Timestamp.valueOf(dateRange.getStartDate()));
        ps.setTimestamp(6, Timestamp.valueOf(dateRange.getEndDate()));
        setPaidSessionParameters(ps, session);
    }

    private void setPaidSessionParameters(PreparedStatement ps, Session session) throws SQLException {
        if (session instanceof PaidSession) {
            PaidSession paidSession = (PaidSession) session;
            ps.setLong(7, paidSession.getPrice());
            ps.setInt(8, paidSession.getMaxParticipants());
        } else {
            ps.setNull(7, Types.BIGINT);
            ps.setNull(8, Types.INTEGER);
        }
    }

    private void saveCoverImage(CoverImage coverImage, Long sessionId) {
        String sql = "INSERT INTO cover_image (session_id, file_size, file_extension, width, height) " +
                "VALUES (?, ?, ?, ?, ?)";

        CoverImageDimension dimension = coverImage.getDimension();
        jdbcTemplate.update(sql,
                sessionId,
                coverImage.getFileSize(),
                coverImage.getFileExtension(),
                dimension.getWidth(),
                dimension.getHeight()
        );
    }

    @Override
    public Optional<Session> findById(Long id) {
        String sql = getFindByIdQuery();
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new SessionRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private String getFindByIdQuery() {
        return "SELECT s.id, s.course_id, s.title, s.session_type, s.status, " +
                "s.start_date, s.end_date, s.price, s.max_participants, " +
                "c.id as cover_id, c.file_size, c.file_extension, c.width, c.height " +
                "FROM session s " +
                "INNER JOIN cover_image c ON s.id = c.session_id " +
                "WHERE s.id = ?";
    }

    private static class SessionRowMapper implements RowMapper<Session> {
        @Override
        public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
            return createSession(rs);
        }

        private Session createSession(ResultSet rs) throws SQLException {
            SessionType sessionType = SessionType.valueOf(rs.getString("session_type"));
            CoverImage coverImage = createCoverImage(rs);
            LocalDateTime startDate = toLocalDateTime(rs.getTimestamp("start_date"));
            LocalDateTime endDate = toLocalDateTime(rs.getTimestamp("end_date"));

            return sessionType == SessionType.FREE ?
                    createFreeSession(rs, coverImage, startDate, endDate) :
                    createPaidSession(rs, coverImage, startDate, endDate);
        }

        private FreeSession createFreeSession(ResultSet rs, CoverImage coverImage,
                                              LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
            return new FreeSession(
                    rs.getLong("id"),
                    rs.getLong("course_id"),
                    rs.getString("title"),
                    coverImage,
                    startDate,
                    endDate
            );
        }

        private PaidSession createPaidSession(ResultSet rs, CoverImage coverImage,
                                              LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
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
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
