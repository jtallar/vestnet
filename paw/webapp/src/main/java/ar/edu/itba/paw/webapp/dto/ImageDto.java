package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.webapp.config.WebConfig;

import javax.validation.constraints.Size;

public class ImageDto {

    private long id;

    @Size(max = WebConfig.MAX_UPLOAD_SIZE)
    private byte[] image;
    private boolean main;

    public static ImageDto fromUserImage(UserImage userImage) {
        final ImageDto imageDto = new ImageDto();
        if (userImage != null) imageDto.setImage(userImage.getImage());
        return imageDto;
    }

    public static ImageDto fromProjectImage(ProjectImage projectImage) {
        final ImageDto imageDto = new ImageDto();

        imageDto.setImage(projectImage.getImage());
        imageDto.setId(projectImage.getId());
        imageDto.setMain(projectImage.isMain());

        return imageDto;
    }

    /* Getters and setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
