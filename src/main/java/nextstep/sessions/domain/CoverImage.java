package nextstep.sessions.domain;

import java.util.Set;

public class CoverImage {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final Set<String> ALLOWED_EXTENSION = Set.of("gif", "jpg", "jpeg", "png", "svg");
    private static final String VECTOR_EXTENSION = "svg";
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 200;
    private static final double ASPECT_RATIO = 3.0 / 2.0;

    private final int fileSize;
    private final String fileExtension;
    private final int width;
    private final int height;

    public CoverImage() {
        this(100, "jpg", 300, 200);
    }

    public CoverImage(int fileSize, String fileExtension, int width, int height) {
        this.fileSize = fileSize;
        this.fileExtension = fileExtension.toLowerCase();
        this.width = width;
        this.height = height;
        validate();
    }

    private void validate() {
        validateFileSize();
        validateFileExtension();
        if (!isVectorImage()) {
            validateImageDimensions();
        }
    }

    private void validateFileSize() {
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }

    private void validateFileExtension() {
        if (!ALLOWED_EXTENSION.contains(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않는 이미지 확장자입니다.");
        }
    }

    private boolean isVectorImage() {
        return fileExtension.equals(VECTOR_EXTENSION);
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
