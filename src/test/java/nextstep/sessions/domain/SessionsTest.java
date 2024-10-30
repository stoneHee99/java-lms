package nextstep.sessions.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SessionsTest {


    @DisplayName("Sessions가 잘 생성되는지")
    @Test
    void testCreate() throws {
        List<Session> sessionList = List.of(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        new CoverImage(),
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new PaidSession(  "우아한형제들과 함께하는 TDD LiveLecture",
                        200000,
                        20,
                        new CoverImage(),
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );
        Sessions sessions = new Sessions(sessionList);
        assertThat(sessions).isNotNull();
    }

    @DisplayName("Sessions에서 꺼낸 컬렉션 수정시 예외가 발생하는지")
    @Test
    void testUnmodifiableList() {
        List<Session> sessionList = List.of(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        new CoverImage(),
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new PaidSession(  "우아한형제들과 함께하는 TDD LiveLecture",
                        200000,
                        20,
                        new CoverImage(),
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );

        Sessions sessions = new Sessions(sessionList);
        List<Session> sessionsList = sessions.getSessions();
        assertThat(sessionsList).isNotNull();
        assertThatThrownBy(() -> sessionsList.add(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        new CoverImage(),
                        LocalDateTime.now(),
                        LocalDateTime.now())))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
