package nextstep.sessions.domain;

public class CoverImageFileSize {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private final int value;

    public CoverImageFileSize(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }
}
