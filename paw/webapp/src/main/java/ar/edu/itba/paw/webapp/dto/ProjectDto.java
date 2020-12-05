package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Project;
import org.glassfish.jersey.server.Uri;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class ProjectDto {

    private long id;

    @Size(min = 5, max = 50)
    @NotBlank
    private String name;

    @Size(min = 50, max = 250)
    @NotBlank
    private String summary;

    @Min(1000)
    @Max(9999999)
    private long cost;

    private boolean funded;
    private Date publishDate;
    private Date updateDate;
    private long hits;
    private long msgCount;

    private URI categories;
    private URI owner;
    private URI portraitImage;
    private URI slideshowImages;

    public static ProjectDto fromProject(Project project, UriInfo uriInfo) {
        final ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setSummary(project.getSummary());
        projectDto.setCost(project.getCost());
        projectDto.setFunded(project.isFunded());
        projectDto.setPublishDate(project.getPublishDate());
        projectDto.setUpdateDate(project.getUpdateDate());
        projectDto.setHits(project.getHits());
        projectDto.setMsgCount(project.getMsgCount());

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        if (uriInfo.getPathParameters().isEmpty()) builder.path(String.valueOf(projectDto.id));
        builder.path("categories");
        projectDto.setCategories(builder.build());

        projectDto.setOwner(uriInfo.getBaseUriBuilder().path("/users").path(String.valueOf(project.getOwnerId())).build());
        projectDto.setPortraitImage(uriInfo.getBaseUriBuilder().path("/images/projects").path(String.valueOf(projectDto.id)).build());
        projectDto.setSlideshowImages(uriInfo.getBaseUriBuilder().path("/images/projects").path(String.valueOf(projectDto.id)).path("/slideshow").build());

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

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public boolean isFunded() {
        return funded;
    }

    public void setFunded(boolean funded) {
        this.funded = funded;
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

    public long getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(long msgCount) {
        this.msgCount = msgCount;
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
}
