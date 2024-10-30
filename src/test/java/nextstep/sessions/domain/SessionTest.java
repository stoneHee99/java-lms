package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
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

        assertThatNoException().isThrownBy(() -> session.enroll(new Payment("id", 1L, 2L, 0L), NsUserTest.SANJIGI));
    }

    @DisplayName("강의 수강을 신청했을 때 모집 중이지 않은 경우 예외가 발생하는지")
    @Test
    void enroll_whenNotRecruiting() throws IOException {
        Session session = new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        assertThatThrownBy(() -> session.enroll(new Payment("id", 1L, 2L, 0L), NsUserTest.JAVAJIGI))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @DisplayName("유료 강의가 최대 수강 인원을 초과하지 않은 경우 수강 신청이 잘 되는지")
    @Test
    void enroll_whenNotExceedMaxEnrolledUserCount() throws IOException {
        Session session = new PaidSession("자바지기와 함께하는 자바 LiveLecture",
                5000L,
                1,
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        session.startRecruitment();

        assertThatNoException().isThrownBy(() -> session.enroll(new Payment("id", 1L, 2L, 5000L), NsUserTest.JAVAJIGI));
    }

    @DisplayName("유료 강의가 최대 수강 인원을 초과한 경우 예외가 잘 발생하는지")
    @Test
    void enroll_whenExceedMaxEnrolledUserCount() throws IOException {
        Session session = new PaidSession("자바지기와 함께하는 자바 LiveLecture",
                5000L,
                1,
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        session.startRecruitment();
        session.enroll(new Payment("id", 1L, 2L, 5000L), NsUserTest.SANJIGI);

        assertThatThrownBy(() -> session.enroll(new Payment("id", 1L, 2L, 5000L), NsUserTest.JAVAJIGI))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @DisplayName("유료 강의가 결제 금액과 수강료가 일치하지 않는 경우 예외가 잘 발생하는지")
    @Test
    void enroll_whenNotMatchPrice() throws IOException {
        Session session = new PaidSession("자바지기와 함께하는 자바 LiveLecture",
                5000L,
                1,
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        session.startRecruitment();

        assertThatThrownBy(() -> session.enroll(new Payment("id", 1L, 2L, 1000L), NsUserTest.JAVAJIGI))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}