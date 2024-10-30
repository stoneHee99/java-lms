package nextstep.sessions.domain;

import java.util.Arrays;
import java.util.Set;

public class CoverImage {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 200;
    private static final double ASPECT_RATIO = 3.0 / 2.0;

    private final int fileSize;
    private final CoverImageExtension fileExtension;
    private final int width;
    private final int height;

    public CoverImage() {
        this(100, "jpg", 300, 200);
    }

    public CoverImage(int fileSize, String fileExtension, int width, int height) {
        this.fileSize = fileSize;
        this.fileExtension = CoverImageExtension.parseExtension(fileExtension);
        this.width = width;
        this.height = height;
        validate();
    }

    private void validate() {
        validateFileSize();
        if (!isVectorImage()) {
            validateImageDimensions();
        }
    }

    private void validateFileSize() {
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }

    private boolean isVectorImage() {
        return fileExtension.isVectorImage();
    }

    private void validateImageDimensions() {
        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            throw new IllegalArgumentException("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
        }
        double ratio = (double) width / height;
        if (ratio != ASPECT_RATIO) {
            throw new IllegalArgumentException("이미지 비율은 3:2 여야 합니다.");
        }
    }
}
