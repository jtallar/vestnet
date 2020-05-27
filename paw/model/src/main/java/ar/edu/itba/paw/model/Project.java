package ar.edu.itba.paw.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Models a project with all its properties.
 */
public class Project {
    private final long id;
    private final String name;

    private final String summary;
    private final LocalDate publishDate;
    private final LocalDate updateDate;
    private final long cost;
    private final long hits;

    // Si quiero poder inicializarlo despues, poner tamb final ownerId y sacarle el final a owner
    private final long ownerUserId;
    private User owner;
    private final ProjectBackOffice backOffice;
    private List<Category> categories;
    private final List<Long> stageIds;
    private List<Stage> stages;
    private Integer notRead; // TODO: SACAR DE ACA, pasarlo a metodo que recibe lista de ids como en favs

    public Integer getNotRead() {
        return notRead;
    }

    public void setNotRead(Integer notRead) {
        this.notRead = notRead;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   User owner, ProjectBackOffice backOffice, List<Category> categories, List<Long> stageIds) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.owner = owner;
        this.ownerUserId = owner.getId();
        this.backOffice = backOffice;
        this.categories = categories;
        this.stageIds = stageIds;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   User owner, ProjectBackOffice backOffice, List<Category> categories, List<Long> stageIds, List<Stage> stages) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.owner = owner;
        this.ownerUserId = owner.getId();
        this.backOffice = backOffice;
        this.categories = categories;
        this.stageIds = stageIds;
        this.stages = stages;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   User owner, ProjectBackOffice backOffice, List<Category> categories, List<Long> stageIds, List<Stage> stages, Integer notRead) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.owner = owner;
        this.ownerUserId = owner.getId();
        this.backOffice = backOffice;
        this.categories = categories;
        this.stageIds = stageIds;
        this.stages = stages;
        this.notRead = notRead;
    }

    public Project(long id, String name, String summary, LocalDate publishDate, LocalDate updateDate, long cost, long hits,
                   long ownerUserId, ProjectBackOffice backOffice, List<Category> categories, List<Long> stageIds) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.publishDate = publishDate;
        this.updateDate = updateDate;
        this.cost = cost;
        this.hits = hits;
        this.ownerUserId = ownerUserId;
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

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public long getCost() {
        return cost;
    }

    public long getHits() {
        return hits;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public ProjectBackOffice getBackOffice() {
        return backOffice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Long> getStageIds() {
        return stageIds;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public boolean hasCategory(String cat){
        boolean[] hasIt = {false};
        this.getCategories().forEach(category -> {
            if(category.getName().equals(cat)) hasIt[0] = true;
        });
        return hasIt[0];
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", publishDate=" + publishDate +
                ", updateDate=" + updateDate +
                ", cost=" + cost +
                ", hits=" + hits +
                ", ownerUserId=" + ownerUserId +
                ", owner=" + owner +
                ", backOffice=" + backOffice +
                ", categories=" + categories +
                ", stageIds=" + stageIds +
                ", stages=" + stages +
                '}';
    }

    /**
     * Model for back office control.
     * Saved for later implementation.
     */
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

        @Override
        public String toString() {
            return "ProjectBackOffice{" +
                    "approved=" + approved +
                    ", profitIndex=" + profitIndex +
                    ", riskIndex=" + riskIndex +
                    '}';
        }
    }
}
