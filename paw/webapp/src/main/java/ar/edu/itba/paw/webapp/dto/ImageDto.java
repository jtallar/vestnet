package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;

public class ImageDto {
    private byte[] image;

    public static ImageDto fromUserImage(UserImage userImage) {
        final ImageDto imageDto = new ImageDto();
        if (userImage == null) return imageDto;
        imageDto.image = userImage.getImage();
        return imageDto;
    }

    public static ImageDto fromProjectImage(ProjectImage projectImage) {
        final ImageDto imageDto = new ImageDto();
        imageDto.image = projectImage.getImage();
        return imageDto;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
