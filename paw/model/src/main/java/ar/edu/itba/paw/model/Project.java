package ar.edu.itba.paw.model;

import java.util.Date;
import java.util.List;

public class Project {
    private final long id;
    private final String name;

    private final String summary;
    private final Date publishDate;
    private final Date updateDate;
    private final long cost;
    private final long hits;
    private final boolean hasImages;

    // Si quiero poder inicializarlo despues, poner tamb final ownerId y sacarle el final a owner
    private final User owner;
    private final ProjectBackOffice backOffice;
    private final List<Category> categories;
    private final List<Integer> stageIds;
    private List<Stage> stages;

    public Project(long id, String name, String summary, Date publishDate, Date updateDate, long cost, long hits, boolean hasImages, User owner, ProjectBackOffice backOffice, List<Category> categories, List<Integer> stageIds) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.hasImages = hasImages;
        this.owner = owner;
        this.backOffice = backOffice;
        this.categories = categories;
        this.stageIds = stageIds;
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

    public Date getPublishDate() {
        return publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public long getCost() {
        return cost;
    }

    public long getHits() {
        return hits;
    }

    public boolean isHasImages() {
        return hasImages;
    }

    public User getOwner() {
        return owner;
    }

    public ProjectBackOffice getBackOffice() {
        return backOffice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Integer> getStageIds() {
        return stageIds;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public static class ProjectBackOffice {
        private final boolean approved;
        private final int profitIndex;
        private final int riskIndex;

        public ProjectBackOffice(boolean approved, int profitIndex, int riskIndex) {
            this.approved = approved;
            this.profitIndex = profitIndex;
            this.riskIndex = riskIndex;
        }

        public boolean isApproved() {
            return approved;
        }

        public int getProfitIndex() {
            return profitIndex;
        }

        public int getRiskIndex() {
            return riskIndex;
        }
    }
}
