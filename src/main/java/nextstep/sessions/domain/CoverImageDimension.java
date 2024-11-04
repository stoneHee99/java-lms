package nextstep.sessions.domain;

public class CoverImageDimension {

    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 200;
    private static final double ASPECT_RATIO = 3.0 / 2.0;

    private final int width;
    private final int height;

    public CoverImageDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void validate() {
        validateMinimumSize(width, height);
        validateAspectRatio(width, height);
    }

    private void validateMinimumSize(int width, int height) {
        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            throw new IllegalArgumentException("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
        }
    }

    private void validateAspectRatio(int width, int height) {
        double ratio = (double) width / height;
        if (ratio != ASPECT_RATIO) {
            throw new IllegalArgumentException("이미지 비율은 3:2 여야 합니다.");
        }
    }
}
