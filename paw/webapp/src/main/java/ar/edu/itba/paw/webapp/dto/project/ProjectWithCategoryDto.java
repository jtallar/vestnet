package ar.edu.itba.paw.webapp.dto.project;

import ar.edu.itba.paw.webapp.dto.CategoryDto;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

public class ProjectWithCategoryDto {
    private long id;

    @Size(min = 5, max = 50)
    @NotBlank
    private String name;

    @Size(min = 30, max = 250)
    @NotBlank
    private String summary;

    @Min(1000)
    @Max(2000000000)
    private long fundingTarget;

    @NotEmpty
    @Valid
    private List<CategoryDto> categories;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getFundingTarget() {
        return fundingTarget;
    }

    public void setFundingTarget(long fundingTarget) {
        this.fundingTarget = fundingTarget;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
