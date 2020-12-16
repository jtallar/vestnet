package ar.edu.itba.paw.webapp.dto.project;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.image.ProjectImage;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectDto {

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

    @Min(1000)
    @Max(2000000000)
    private long fundingCurrent;

    private boolean closed;
    private Date publishDate;
    private Date updateDate;
    private long hits;

    private boolean portraitExists, slideshowExists;

    private URI categories;
    private URI owner;
    private URI portraitImage;
    private URI slideshowImages;

    public static ProjectDto fromProject(Project project, UriInfo uriInfo) {
        final ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setSummary(project.getSummary());
        projectDto.setFundingTarget(project.getFundingTarget());
        projectDto.setFundingCurrent(project.getFundingCurrent());
        projectDto.setClosed(project.isClosed());
        projectDto.setPublishDate(project.getPublishDate());
        projectDto.setUpdateDate(project.getUpdateDate());
        projectDto.setHits(project.getHits());

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        if (uriInfo.getPathParameters().isEmpty()) builder.path(String.valueOf(projectDto.id));
        builder.path("categories");
        projectDto.setCategories(builder.build());

        projectDto.setOwner(uriInfo.getBaseUriBuilder().path("/users").path(String.valueOf(project.getOwnerId())).build());

        final Map<Boolean, Long> imageCount = project.getImages().stream().map(ProjectImage::isMain).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Optional.ofNullable(imageCount.get(true)).ifPresent(aLong -> {
            projectDto.portraitExists = true;
            projectDto.setPortraitImage(uriInfo.getBaseUriBuilder().path("/images/projects").path(String.valueOf(projectDto.id)).build());
        });
        Optional.ofNullable(imageCount.get(false)).ifPresent(aLong -> {
            projectDto.slideshowExists = true;
            projectDto.setSlideshowImages(uriInfo.getBaseUriBuilder().path("/images/projects").path(String.valueOf(projectDto.id)).path("/slideshow").build());
        });

        return projectDto;
    }

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

    public long getFundingCurrent() {
        return fundingCurrent;
    }

    public void setFundingCurrent(long fundingCurrent) {
        this.fundingCurrent = fundingCurrent;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public URI getCategories() {
        return categories;
    }

    public void setCategories(URI categories) {
        this.categories = categories;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public URI getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(URI portraitImage) {
        this.portraitImage = portraitImage;
    }

    public URI getSlideshowImages() {
        return slideshowImages;
    }

    public void setSlideshowImages(URI slideshowImages) {
        this.slideshowImages = slideshowImages;
    }

    public boolean isPortraitExists() {
        return portraitExists;
    }

    public void setPortraitExists(boolean portraitExists) {
        this.portraitExists = portraitExists;
    }

    public boolean isSlideshowExists() {
        return slideshowExists;
    }

    public void setSlideshowExists(boolean slideshowExists) {
        this.slideshowExists = slideshowExists;
    }
}
