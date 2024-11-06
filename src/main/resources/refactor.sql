-- 강의 모집 상태 요구사항 변경에 따른 기존 데이터 수정 쿼리
ALTER TABLE sessions ADD COLUMN progress_status VARCHAR(20);
ALTER TABLE sessions ADD COLUMN enrollment_status VARCHAR(20);

UPDATE sessions
SET
    progress_status =
        CASE status
            WHEN 'PREPARING' THEN 'PREPARING'
            WHEN 'RECRUITING' THEN 'PREPARING'
            WHEN 'CLOSED' THEN 'FINISHED'
            END,
    enrollment_status =
        CASE status
            WHEN 'PREPARING' THEN 'CLOSED'
            WHEN 'RECRUITING' THEN 'OPEN'
            WHEN 'CLOSED' THEN 'CLOSED'
            END;

ALTER TABLE sessions MODIFY COLUMN progress_status VARCHAR(20) NOT NULL;
ALTER TABLE sessions MODIFY COLUMN enrollment_status VARCHAR(20) NOT NULL;

-- ALTER TABLE sessions DROP COLUMN status;