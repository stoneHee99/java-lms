package nextstep.courses.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoverImageTest {

    @TempDir
    File tempDirectory;

    @DisplayName("1MB 이상의 이미지로 생성시 예외가 발생하는지")
    @Test
    void createCoverImage_withExceeding1MB() throws IOException {
        File largeFile = new File(tempDirectory, "large.jpg");
        byte[] largeData = new byte[1024 * 1024 + 1]; // 1024 * 1024 = 1MB
        Files.write(largeFile.toPath(), largeData);

        assertThatThrownBy(() -> new CoverImage(largeFile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("허용하지 않는 확장자로 생성시 예외가 발생하는지")
    @Test
    void createCoverImage_withNotAllowedExtension() throws IOException {
        File invalidFile = new File(tempDirectory, "invalidFile.txt");
        byte[] notAllowedData = new byte[1024];
        Files.write(invalidFile.toPath(), notAllowedData);

        assertThatThrownBy(() -> new CoverImage(invalidFile))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
