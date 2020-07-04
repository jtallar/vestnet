package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;

public class ImageDto {
    private long imageId;
    private byte[] image;

    private boolean main;

    public static ImageDto fromUserImage(UserImage userImage) {
        final ImageDto imageDto = new ImageDto();
//        imageDto.imageId = userImage.getId();
        imageDto.image = userImage.getImage();
        return imageDto;
    }

    public static ImageDto fromProjectImage(ProjectImage projectImage) {
        final ImageDto imageDto = new ImageDto();
//        imageDto.imageId = projectImage.getId();
        imageDto.image = projectImage.getImage();
        return imageDto;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
