package nextstep.courses.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    @TempDir
    static File tempDirectory;

    static File validImageFile() throws IOException {
        File file = new File(tempDirectory, "coverImage.jpg");
        BufferedImage invalidImage = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(invalidImage, "jpg", file);
        return file;
    }

    @DisplayName("모집 중인 강의에 강의 모집 시작을 요청했을 때 예외가 발생하는지")
    @Test
    void startRecruitment_whenRecruiting() throws IOException {
        Session session = new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        session.startRecruitment();
        assertThatThrownBy(session::startRecruitment)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("강의 수강을 신청했을 때 모집 중인 경우 예외가 발생하지 않는지")
    @Test
    void enroll_whenRecruiting() throws IOException {
        Session session = new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());
        session.startRecruitment();

        session.enroll(new Payment(), NsUserTest.SANJIGI);
    }

    @DisplayName("강의 수강을 신청했을 때 모집 중이지 않은 경우 예외가 발생하는지")
    @Test
    void enroll_whenNotRecruiting() throws IOException {
        Session session = new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        assertThatThrownBy(() -> session.enroll(new Payment(), NsUserTest.JAVAJIGI))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
