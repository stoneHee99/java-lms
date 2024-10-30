package nextstep.sessions.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SessionsTest {

    @TempDir
    static File tempDirectory;

    static File validImageFile() throws IOException {
        File file = new File(tempDirectory, "coverImage.jpg");
        BufferedImage invalidImage = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(invalidImage, "jpg", file);
        return file;
    }

    @DisplayName("Sessions가 잘 생성되는지")
    @Test
    void testCreate() throws IOException {
        List<Session> sessionList = List.of(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        validImageFile(),
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new PaidSession(  "우아한형제들과 함께하는 TDD LiveLecture",
                        200000,
                        20,
                        validImageFile(),
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );
        Sessions sessions = new Sessions(sessionList);
        assertThat(sessions).isNotNull();
    }

    @DisplayName("Sessions에서 꺼낸 컬렉션 수정시 예외가 발생하는지")
    @Test
    void testUnmodifiableList() throws IOException {
        List<Session> sessionList = List.of(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        validImageFile(),
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new PaidSession(  "우아한형제들과 함께하는 TDD LiveLecture",
                        200000,
                        20,
                        validImageFile(),
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );

        Sessions sessions = new Sessions(sessionList);
        List<Session> sessionsList = sessions.getSessions();
        assertThat(sessionsList).isNotNull();
        assertThatThrownBy(() -> sessionsList.add(
                new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                        validImageFile(),
                        LocalDateTime.now(),
                        LocalDateTime.now())))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
