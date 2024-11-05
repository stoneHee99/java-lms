package nextstep.sessions.domain;

import java.util.Arrays;

public enum CoverImageExtension {
    GIF("gif"),
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    SVG("svg");

    private final String extension;

    CoverImageExtension(String extension) {
        this.extension = extension;
    }

    public boolean isVectorImage() {
        return this == SVG;
    }

    public static CoverImageExtension parseExtension(String extension) {
        return Arrays.stream(values())
                .filter(e -> e.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("허용되지 않는 이미지 확장자입니다."));
    }

    public String getExtension() {
        return extension;
    }
}
