package nextstep.courses.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CoverImage {

    private static final int MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final Set<String> ALLOWED_EXTENSION = Set.of("gif", "jpg", "jpeg", "png", "svg");
    private static final String VECTOR_EXTENSION = "svg";
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 200;
    private static final double ASPECT_RATIO = 3.0 / 2.0;

    private final File imageFile;

    public CoverImage(File imageFile) {
        this.imageFile = imageFile;
        valid();
    }

    private void valid() {
        validFileSize();
        validFileExtension();
        if (!isVectorImage()) {
            validImageDimension();
        }
    }

    private void validFileSize() {
        if (imageFile.length() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 1MB를 넘을 수 없습니다.");
        }
    }

    private void validFileExtension() {
        String extension = getFileExtension();
        if (!ALLOWED_EXTENSION.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 이미지 확장자입니다.");
        }
    }

    private String getFileExtension() {
        String fileName = imageFile.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isVectorImage() {
        return getFileExtension().equals(VECTOR_EXTENSION);
    }

    private void validImageDimension() {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            validImageSize(image);
            validImageRatio(image);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일이 손상되었습니다.");
        }
    }

    private void validImageSize(BufferedImage image) {
        if (image.getWidth() < MIN_WIDTH || image.getHeight() < MIN_HEIGHT) {
            throw new IllegalArgumentException("이미지 크기는 최소 300 x 200 이상이어야 합니다.");
        }
    }

    private void validImageRatio(BufferedImage image) {
        double ratio = (double) image.getWidth() / image.getHeight();
        if (ratio != ASPECT_RATIO) {
            throw new IllegalArgumentException("이미지 비율은 3:2 여야 합니다.");
        }
    }
}
