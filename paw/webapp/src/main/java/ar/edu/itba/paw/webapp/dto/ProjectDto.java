package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Project;

import javax.ws.rs.core.UriInfo;
import java.util.Date;

public class ProjectDto {

    private long id;
    private String name;
    private String summary;
    private long cost;
    private boolean funded;
    private Date publishDate;
    private Date updateDate;
    private long hits;
    private long msgCount;

//    private Uri owner; // Lazy fetching
//    private List<ProjectImage> images; // Lazy fetching
//    private List<Category> categories; // Lazy fetching
//    private List<Message> messageList; // Lazy fetching
//    private List<User> favoriteBy; // Lazy fetching

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
}
