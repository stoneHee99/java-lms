package nextstep.sessions.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class CoverImageTest {

    @DisplayName("1MB 이상의 크기면 예외가 발생하는지")
    @Test
    void createCoverImage_withExceeding1MB() {
        int largeFileSize = 1024 * 1024 + 1; // 1MB + 1 byte

        assertThatThrownBy(() -> new CoverImage(largeFileSize, "jpg", 300, 200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
    }

    @DisplayName("허용된 확장자로 생성 시 잘 생성되는지")
    @ParameterizedTest
    @ValueSource(strings = {"jpg", "png", "gif", "jpeg", "svg"})
    void createCoverImage_withAllowedExtension(String extension) {
        int validSize = 1024 * 500; // 500KB

        assertThatNoException().isThrownBy(() -> new CoverImage(validSize, extension, 300, 200));
    }

    @DisplayName("허용되지 않은 확장자면 예외가 발생하는지")
    @Test
    void createCoverImage_withNotAllowedExtension() {
        int validSize = 1024 * 500;

        assertThatThrownBy(() -> new CoverImage(validSize, "txt", 300, 200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("허용되지 않는 이미지 확장자입니다.");
    }

    @DisplayName("비율이 3:2가 아니면 예외가 발생하는지")
    @Test
    void createCoverImage_withInvalidRatio() {
        assertThatThrownBy(() -> new CoverImage(1024 * 500, "jpg", 300, 250))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 비율은 3:2 여야 합니다.");
    }

    @DisplayName("너비가 300보다 작으면 예외가 발생하는지")
    @Test
    void createCoverImage_withWidthLessThan300() {
        assertThatThrownBy(() -> new CoverImage(1024 * 500, "jpg", 299, 200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
    }

    @DisplayName("높이가 200보다 작으면 예외가 발생하는지")
    @Test
    void createCoverImage_withHeightLessThan200() {
        assertThatThrownBy(() -> new CoverImage(1024 * 500, "jpg", 300, 199))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
    }
}
