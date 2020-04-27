package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.webapp.config.WebConfig;
import cz.jirutka.validator.spring.SpELAssert;
import org.hibernate.validator.constraints.Length;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.AutoPopulatingList;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;



public class NewProjectFields {

    @Size(min = 0, max = 25)
    private String title;

    @Size(min = 0, max = 250)
    private String summary;

    // TODO: Delete when stages are ready
    private int cost;

    @NotEmpty(message = "List cannot be empty")
    private List<Long> categories;

    // TODO: SEGUN COMO CAPTURE LA EXCEPCION, VER SI HACE FALTA EL PARAM
    @ImageFile(maxSize = WebConfig.MAX_UPLOAD_SIZE)
    private MultipartFile image;

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

    public MultipartFile getImage() {
        return image;
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

    public void setImage(MultipartFile image) {
        this.image = image;
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
