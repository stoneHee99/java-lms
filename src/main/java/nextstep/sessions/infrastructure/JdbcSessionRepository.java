package nextstep.sessions.infrastructure;

import java.util.ArrayList;
import nextstep.sessions.domain.*;
import nextstep.users.domain.NsUser;
import nextstep.users.domain.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        Long sessionId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        for (CoverImage coverImage : session.getCoverImages()) {
            saveCoverImage(coverImage, sessionId);
        }
        return sessionResult;
    }

    private int saveSession(Session session, KeyHolder keyHolder) {
        String sql =
                "INSERT INTO session (course_id, title, session_type, progress_status, recruitment_status, "
                        +
                        "start_date, end_date, price, max_participants) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            setSessionParameters(ps, session);
            return ps;
        }, keyHolder);
    }

    private void setSessionParameters(PreparedStatement ps, Session session) throws SQLException {
        DateRange dateRange = session.getSessionDateRange();
        ps.setLong(1, session.getCourseId());
        ps.setString(2, session.getTitle());
        ps.setString(3, session.getSessionType().name());
        ps.setString(4, session.getProgressStatus().name());
        ps.setString(5, session.getRecruitmentStatus().name());
        ps.setTimestamp(6, Timestamp.valueOf(dateRange.getStartDate()));
        ps.setTimestamp(7, Timestamp.valueOf(dateRange.getEndDate()));
        setPaidSessionParameters(ps, session);
    }

    private void setPaidSessionParameters(PreparedStatement ps, Session session)
            throws SQLException {
        if (session instanceof PaidSession) {
            PaidSession paidSession = (PaidSession) session;
            ps.setLong(8, paidSession.getPrice());
            ps.setInt(9, paidSession.getMaxParticipants());
        } else {
            ps.setNull(8, Types.BIGINT);
            ps.setNull(9, Types.INTEGER);
        }
    }

    private void saveCoverImage(CoverImage coverImage, Long sessionId) {
        String sql =
                "INSERT INTO cover_image (session_id, file_size, file_extension, width, height) " +
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
        Session session = findSessionById(id);
        if (session == null) {
            return Optional.empty();
        }

        List<CoverImage> coverImages = findCoverImagesBySessionId(id);
        session.setCoverImages(coverImages);

        List<Enrollment> enrollments = findEnrollmentsBySessionId(id);
        session.setEnrollments(enrollments);

        return Optional.of(session);
    }

    private Session findSessionById(Long id) {
        String sql =
                "SELECT id, course_id, title, session_type, progress_status, recruitment_status, " +
                        "start_date, end_date, price, max_participants " +
                        "FROM session WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new SessionRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private List<CoverImage> findCoverImagesBySessionId(Long sessionId) {
        String sql = "SELECT id, file_size, file_extension, width, height " +
                "FROM cover_image WHERE session_id = ?";

        return jdbcTemplate.query(sql, new CoverImageRowMapper(), sessionId);
    }

    private List<Enrollment> findEnrollmentsBySessionId(Long sessionId) {
        String sql = "SELECT id, session_id, user_id, enrolled_at, approval_status " +
                "FROM enrollment WHERE session_id = ?";

        return jdbcTemplate.query(sql, new EnrollmentRowMapper(), sessionId);
    }

    private static class SessionRowMapper implements RowMapper<Session> {

        @Override
        public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
            SessionType sessionType = SessionType.valueOf(rs.getString("session_type"));
            LocalDateTime startDate = toLocalDateTime(rs.getTimestamp("start_date"));
            LocalDateTime endDate = toLocalDateTime(rs.getTimestamp("end_date"));

            if (sessionType == SessionType.FREE) {
                return new FreeSession(
                        rs.getLong("id"),
                        rs.getLong("course_id"),
                        rs.getString("title"),
                        new ArrayList<>(),
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
                    new ArrayList<>(),
                    startDate,
                    endDate
            );
        }
    }

    private static class EnrollmentRowMapper implements RowMapper<Enrollment> {

        @Override
        public Enrollment mapRow(ResultSet rs, int rowNum) throws SQLException {
            NsUser user = new NsUser(
                    rs.getLong("user_id"),
                    "user" + rs.getLong("user_id") + "@example.com"
            );

            return new Enrollment(
                    rs.getLong("id"),
                    rs.getLong("session_id"),
                    user,
                    toLocalDateTime(rs.getTimestamp("enrolled_at"))
            );
        }
    }

    private static class CoverImageRowMapper implements RowMapper<CoverImage> {

        @Override
        public CoverImage mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CoverImage(
                    rs.getInt("file_size"),
                    rs.getString("file_extension"),
                    rs.getInt("width"),
                    rs.getInt("height")
            );
        }
    }

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

}
