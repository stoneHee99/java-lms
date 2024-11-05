package nextstep.sessions.domain;

public class CoverImage {

    private final CoverImageFileSize fileSize;
    private final CoverImageExtension fileExtension;
    private final CoverImageDimension dimension;

    public CoverImage(CoverImageFileSize fileSize, CoverImageExtension fileExtension, CoverImageDimension dimension) {
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.dimension = dimension;
        validate();
    }

    public CoverImage(int fileSize, String fileExtension, int width, int height) {
        this(new CoverImageFileSize(fileSize),
                CoverImageExtension.parseExtension(fileExtension),
                new CoverImageDimension(width, height));
    }

    private void validate() {
        if (!isVectorImage()) {
            validateImageDimensions();
        }
    }

    private boolean isVectorImage() {
        return fileExtension.isVectorImage();
    }

    private void validateImageDimensions() {
        dimension.validate();
    }
}
