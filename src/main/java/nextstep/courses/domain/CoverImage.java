package nextstep.courses.domain;

import java.io.File;
import java.util.Set;

public class CoverImage {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final Set<String> ALLOWED_EXTENSION = Set.of("gif", "jpg", "jpeg", "png", "svg");

    private final File imageFile;

    public CoverImage(File imageFile) {
        this.imageFile = imageFile;
        valid();
    }

    private void valid() {
        validFileSize();
        validFileExtension();
    }

    private void validFileSize() {
        if (imageFile.length() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }

    private void validFileExtension() {
        String fileName = imageFile.getName();
        String extension = fileName
                .substring(fileName.lastIndexOf(".") + 1)
                .toLowerCase();

        if (!ALLOWED_EXTENSION.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 이미지 확장자입니다.");
        }
    }
}
