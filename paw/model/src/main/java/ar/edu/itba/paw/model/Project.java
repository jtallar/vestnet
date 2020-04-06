package ar.edu.itba.paw.model;

import java.util.Date;

public class Project {
    private final long id;
    private String name;
    private String summary;
    private Date date;
    private final long ownerId;

    public Project(long id, String name, String summary, long ownerId, Date date) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.ownerId = ownerId;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
