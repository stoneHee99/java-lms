package nextstep.courses.domain;

import java.io.File;

public class CoverImage {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB

    private final File imageFile;

    public CoverImage(File imageFile) {
        valid(imageFile);
        this.imageFile = imageFile;
    }

    private void valid(File imageFile) {
        if (imageFile.length() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }
}
