package nextstep.courses.domain;

import nextstep.payments.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("강의 수강을 신청했을 때 모집 중이지 않은 경우 예외가 발생하는지")
    @Test
    void enroll_whenNotRecruiting() throws IOException {
        Session session = new FreeSession("자바지기와 함께하는 자바 LiveLecture",
                validImageFile(),
                LocalDateTime.now(),
                LocalDateTime.now());

        assertThatThrownBy(() -> session.enroll(new Payment()))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
