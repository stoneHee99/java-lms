package nextstep.sessions.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sessions {
    private final List<Session> sessions;

    public Sessions() {
        this.sessions = new ArrayList<>();
    }

    public Sessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }
}