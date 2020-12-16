package ar.edu.itba.paw.webapp.dto.project;

import ar.edu.itba.paw.model.ProjectStats;

import javax.validation.constraints.Min;
import java.util.Date;

public class ProjectStatsDto {

    @Min(1)
    private long seen;

    @Min(1)
    private long secondsAvg;

    @Min(1)
    private long clicksAvg;

    @Min(0)
    private long contactClicks;

    private Date lastSeen;

    public static ProjectStatsDto fromProjectStats(ProjectStats projectStats) {
        ProjectStatsDto statsDto = new ProjectStatsDto();
        statsDto.setClicksAvg(projectStats.getClicksAvg());
        statsDto.setSecondsAvg(projectStats.getSecondsAvg());
        statsDto.setSeen(projectStats.getSeen());
        statsDto.setContactClicks(projectStats.getContactClicks());
        statsDto.setLastSeen(projectStats.getLastSeen());
        return statsDto;
    }


    public long getSeen() {
        return seen;
    }

    public void setSeen(long seen) {
        this.seen = seen;
    }

    public long getSecondsAvg() {
        return secondsAvg;
    }

    public void setSecondsAvg(long secondsAvg) {
        this.secondsAvg = secondsAvg;
    }

    public long getClicksAvg() {
        return clicksAvg;
    }

    public void setClicksAvg(long clicksAvg) {
        this.clicksAvg = clicksAvg;
    }

    public long getContactClicks() {
        return contactClicks;
    }

    public void setContactClicks(long contactClicks) {
        this.contactClicks = contactClicks;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}
