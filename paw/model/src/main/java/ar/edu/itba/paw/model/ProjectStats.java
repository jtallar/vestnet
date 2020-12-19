package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "project_stats")
public class ProjectStats {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_stats_id_seq")
    @SequenceGenerator(sequenceName = "project_stats_id_seq", name = "project_stats_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "stats")
    private Project project;

    @Column(name = "seen")
    private long seen;

    @Column(name = "seconds_avg")
    private long secondsAvg;

    @Column(name = "clicks_avg")
    private long clicksAvg;

    @Column(name = "contact_clicks")
    private long contactClicks;

    @Column(name = "investors_seen")
    private long investorsSeen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen")
    private Date lastSeen;

    /** Protected */ ProjectStats() {
        /** For hibernate only */
    }

    public ProjectStats(boolean dummy) {
        this.seen = 0;
        this.secondsAvg = 0;
        this.clicksAvg = 0;
        this.contactClicks = 0;
        this.investorsSeen = 0;
        this.lastSeen = new Date();
    }

    /** Getters and Setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public long getInvestorsSeen() {
        return investorsSeen;
    }

    public void setInvestorsSeen(long investorsSeen) {
        this.investorsSeen = investorsSeen;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setNewSeen(long seconds, long clicks, boolean investor, boolean contact) {
        if (contact) contactClicks++;
        if (investor) investorsSeen++;
        secondsAvg = (secondsAvg * seen + seconds) / (seen + 1);
        clicksAvg = (clicksAvg * seen + clicks) / (seen + 1);
        seen++;
        lastSeen = new Date();
    }


    /** Hash code, equals and to String */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectStats)) return false;
        ProjectStats that = (ProjectStats) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ar.edu.itba.paw.model.ProjectStats{" +
                "id=" + id +
                ", seen=" + seen +
                ", secondsAvg=" + secondsAvg +
                ", clicksAvg=" + clicksAvg +
                ", contactClicks=" + contactClicks +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
