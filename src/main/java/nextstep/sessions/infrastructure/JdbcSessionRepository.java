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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String sql = "INSERT INTO session (course_id, title, session_type, progress_status, enrollment_status, " +
                "start_date, end_date, price, max_participants) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        ps.setString(4, session.getProgressStatus().name());
        ps.setString(5, session.getEnrollmentStatus().name());
        ps.setTimestamp(6, Timestamp.valueOf(dateRange.getStartDate()));
        ps.setTimestamp(7, Timestamp.valueOf(dateRange.getEndDate()));
        setPaidSessionParameters(ps, session);
    }

    private void setPaidSessionParameters(PreparedStatement ps, Session session) throws SQLException {
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
            List<SessionWithCoverImage> results = jdbcTemplate.query(sql, new SessionWithCoverImageRowMapper(), id);

            if (results.isEmpty()) {
                return Optional.empty();
            }

            SessionWithCoverImage first = results.get(0);
            Session session = first.sessionType == SessionType.FREE ?
                    createFreeSession(first) :
                    createPaidSession(first);

            List<CoverImage> coverImages = results.stream()
                    .map(this::createCoverImage)
                    .collect(Collectors.toList());

            session.setCoverImages(coverImages);

            return Optional.of(session);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private String getFindByIdQuery() {
        return "SELECT s.id, s.course_id, s.title, s.session_type, " +
                "s.progress_status, s.enrollment_status, " +
                "s.start_date, s.end_date, s.price, s.max_participants, " +
                "c.id as cover_id, c.file_size, c.file_extension, c.width, c.height " +
                "FROM session s " +
                "INNER JOIN cover_image c ON s.id = c.session_id " +
                "WHERE s.id = ?";
    }

    private static class SessionWithCoverImage {
        private final Long id;
        private final Long courseId;
        private final String title;
        private final SessionType sessionType;
        private final SessionProgressStatus progressStatus;
        private final EnrollmentStatus enrollmentStatus;
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;
        private final Long price;
        private final Integer maxParticipants;
        private final Long coverId;
        private final Integer fileSize;
        private final String fileExtension;
        private final Integer width;
        private final Integer height;

        private SessionWithCoverImage(Long id, Long courseId, String title, SessionType sessionType, SessionProgressStatus progressStatus, EnrollmentStatus enrollmentStatus, LocalDateTime startDate, LocalDateTime endDate, Long price, Integer maxParticipants, Long coverId, Integer fileSize, String fileExtension, Integer width, Integer height) {
            this.id = id;
            this.courseId = courseId;
            this.title = title;
            this.sessionType = sessionType;
            this.progressStatus = progressStatus;
            this.enrollmentStatus = enrollmentStatus;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
            this.maxParticipants = maxParticipants;
            this.coverId = coverId;
            this.fileSize = fileSize;
            this.fileExtension = fileExtension;
            this.width = width;
            this.height = height;
        }
    }

    private static class SessionWithCoverImageRowMapper implements RowMapper<SessionWithCoverImage> {
        @Override
        public SessionWithCoverImage mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SessionWithCoverImage(
                    rs.getLong("id"),
                    rs.getLong("course_id"),
                    rs.getString("title"),
                    SessionType.valueOf(rs.getString("session_type")),
                    SessionProgressStatus.valueOf(rs.getString("progress_status")),
                    EnrollmentStatus.valueOf(rs.getString("enrollment_status")),
                    toLocalDateTime(rs.getTimestamp("start_date")),
                    toLocalDateTime(rs.getTimestamp("end_date")),
                    rs.getLong("price"),
                    rs.getInt("max_participants"),
                    rs.getLong("cover_id"),
                    rs.getInt("file_size"),
                    rs.getString("file_extension"),
                    rs.getInt("width"),
                    rs.getInt("height")
            );
        }


    }

    private FreeSession createFreeSession(SessionWithCoverImage data) {
        return new FreeSession(
                data.id,
                data.courseId,
                data.title,
                createCoverImage(data),
                data.startDate,
                data.endDate
        );
    }

    private PaidSession createPaidSession(SessionWithCoverImage data) {
        return new PaidSession(
                data.id,
                data.courseId,
                data.title,
                data.price,
                data.maxParticipants,
                createCoverImage(data),
                data.startDate,
                data.endDate
        );
    }

    private CoverImage createCoverImage(SessionWithCoverImage data) {
        return new CoverImage(
                data.fileSize,
                data.fileExtension,
                data.width,
                data.height
        );
    }

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

}
