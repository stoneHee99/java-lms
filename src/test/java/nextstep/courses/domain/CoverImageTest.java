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
    void createCoverImageWithExceeding1MB() throws IOException {
        File largeFile = new File(tempDirectory, "large.jpg");
        byte[] largeData = new byte[1024 * 1024 + 1]; // 1024 * 1024 = 1MB
        Files.write(largeFile.toPath(), largeData);

        assertThatThrownBy(() -> new CoverImage(largeFile))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
