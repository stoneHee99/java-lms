package nextstep.courses.domain;

public enum SessionStatus {
    PREPARING {
        @Override
        public SessionStatus nextState() {
            return SessionStatus.RECRUITING;
        }
    }, RECRUITING {
        @Override
        public SessionStatus nextState() {
            return SessionStatus.CLOSED;
        }
    }, CLOSED {
        @Override
        public SessionStatus nextState() {
            throw new UnsupportedOperationException("종료된 세션은 상태 변경이 불가능합니다.");
        }
    };

    public abstract SessionStatus nextState();

    public static boolean CanEnroll(SessionStatus status) {
        return status == RECRUITING;
    }
}
