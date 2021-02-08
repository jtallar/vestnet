package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

public class SlideshowDto {

    @NotEmpty
    @Valid
    private List<ImageDto> slideshow;

    /* Getters and setters */

    public List<ImageDto> getSlideshow() {
        return slideshow;
    }

    public void setSlideshow(List<ImageDto> slideshow) {
        this.slideshow = slideshow;
    }
}
