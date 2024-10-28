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

    private static final double ASPECT_RATIO = 3.0 / 2.0;

    private final File imageFile;

    public CoverImage(File imageFile) {
        this.imageFile = imageFile;
        valid();
    }

    private void valid() {
        validFileSize();
        validFileExtension();
        validImageDimension();
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

    private void validImageDimension() {
        if (isVectorImage()) {
            return;
        }

        try {
            BufferedImage image = ImageIO.read(imageFile);
            validImageRatio(image);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일이 손상되었습니다.");
        }
    }

    private boolean isVectorImage() {
        return getFileExtension().equals(VECTOR_EXTENSION);
    }

    private void validImageRatio(BufferedImage image) {
        double ratio = (double) image.getWidth() / image.getHeight();
        System.out.println(ratio);
        if (ratio != ASPECT_RATIO) {
            throw new IllegalArgumentException("이미지 비율은 3:2 여야 합니다.");
        }
    }
}
