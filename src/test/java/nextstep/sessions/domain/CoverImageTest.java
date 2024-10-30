package nextstep.sessions.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;

class CoverImageTest {

    @TempDir
    File tempDirectory;

    @DisplayName("1MB 이상의 이미지로 생성 시 예외가 발생하는지")
    @Test
    void createCoverImage_withExceeding1MB() throws IOException {
        File largeFile = new File(tempDirectory, "large.jpg");
        byte[] largeData = new byte[1024 * 1024 + 1]; // 1024 * 1024 = 1MB
        Files.write(largeFile.toPath(), largeData);

        assertThatThrownBy(() -> new CoverImage(largeFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
    }

    @DisplayName("허용된 확장자로 생성 시 잘 생성되는지")
    @ParameterizedTest
    @ValueSource(strings = {"jpg", "png", "gif", "jpeg", "svg"})
    void createCoverImage_withAllowedExtension(String extension) throws IOException {
        BufferedImage invalidImage = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        File validFile = new File(tempDirectory, "image." + extension);
        Files.write(validFile.toPath(), new byte[1024]);
        ImageIO.write(invalidImage, extension, validFile);

        assertThatNoException().isThrownBy(() -> new CoverImage(validFile));
    }

    @DisplayName("허용하지 않는 확장자로 생성 시 예외가 발생하는지")
    @Test
    void createCoverImage_withNotAllowedExtension() throws IOException {
        File invalidFile = new File(tempDirectory, "invalidFile.txt");
        Files.write(invalidFile.toPath(), new byte[1024]);

        assertThatThrownBy(() -> new CoverImage(invalidFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("허용되지 않는 이미지 확장자입니다.");
    }

    @DisplayName("비율이 3:2가 아닌 이미지 생성 시 예외가 발생하는지")
    @Test
    void createCoverImage_withInvalidRatio() throws IOException {
        BufferedImage invalidImage = new BufferedImage(300, 250, BufferedImage.TYPE_INT_RGB);
        File invalidFile = new File(tempDirectory, "invalidImage.jpg");
        ImageIO.write(invalidImage, "jpg", invalidFile);

        assertThatThrownBy(() -> new CoverImage(invalidFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 비율은 3:2 여야 합니다.");
    }

    @DisplayName("너비가 300보다 작은 이미지 생성 시 예외가 발생하는지")
    @Test
    void createCoverImage_withWidthLessThan300() throws IOException {
        BufferedImage invalidImage = new BufferedImage(299, 200, BufferedImage.TYPE_INT_RGB);
        File invalidFile = new File(tempDirectory, "invalidImage.jpg");
        ImageIO.write(invalidImage, "jpg", invalidFile);

        assertThatThrownBy(() -> new CoverImage(invalidFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
    }

    @DisplayName("높이가 200보다 작은 이미지 생성 시 예외가 발생하는지")
    @Test
    void createCoverImage_withHeightLessThan200() throws IOException {
        BufferedImage invalidImage = new BufferedImage(300, 199, BufferedImage.TYPE_INT_RGB);
        File invalidFile = new File(tempDirectory, "invalidImage.jpg");
        ImageIO.write(invalidImage, "jpg", invalidFile);

        assertThatThrownBy(() -> new CoverImage(invalidFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
    }
}