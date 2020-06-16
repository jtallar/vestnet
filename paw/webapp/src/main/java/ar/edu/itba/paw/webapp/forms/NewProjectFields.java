package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.forms.validators.ImageFile;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;



public class NewProjectFields {

    @Size(min = 5, max = 50)
    private String title;

    @Size(min = 50, max = 250)
    private String summary;

    @Min(1000)
    @Max(9999999)
    private int cost;

    @NotEmpty(message = "List cannot be empty")
    private List<Long> categories;

    @ImageFile(maxSize = WebConfig.MAX_UPLOAD_SIZE)
    private MultipartFile portraitImage;

    @ImageFile(maxSize = WebConfig.MAX_UPLOAD_SIZE, maxCount = WebConfig.MAX_SLIDESHOW_COUNT)
    private List<MultipartFile> slideshowImages;

    /** Getters and setters */

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getCost() {
        return cost;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public MultipartFile getPortraitImage() {
        return portraitImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public void setPortraitImage(MultipartFile portraitImage) {
        this.portraitImage = portraitImage;
    }

    public List<MultipartFile> getSlideshowImages() {
        return slideshowImages;
    }

    public void setSlideshowImages(List<MultipartFile> slideshowImages) {
        this.slideshowImages = slideshowImages;
    }

    @Override
    public String toString() {
        return "NewProjectFields{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", cost=" + cost +
                ", categories=" + categories +
                '}';
    }
}