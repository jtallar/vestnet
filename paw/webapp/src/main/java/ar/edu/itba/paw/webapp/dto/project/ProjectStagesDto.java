package ar.edu.itba.paw.webapp.dto.project;

import ar.edu.itba.paw.model.ProjectStages;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.Date;

public class ProjectStagesDto {

    @Min(1)
    private long number;

    @NotEmpty
    private String name;

    private String comment;

    private boolean completed;

    private Date completedDate;


    public static ProjectStagesDto fromProjectStages(ProjectStages projectStages) {
        ProjectStagesDto stagesDto = new ProjectStagesDto();

        stagesDto.setNumber(projectStages.getNumber());
        stagesDto.setName(projectStages.getName());
        stagesDto.setComment(projectStages.getComment());
        stagesDto.setCompleted(projectStages.isCompleted());
        if (stagesDto.isCompleted()) stagesDto.setCompletedDate(projectStages.getCompletedDate());

        return stagesDto;
    }


    /* Getters and Setters */

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
