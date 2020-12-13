package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Models a message. Used for communication between users.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id_seq")
    @SequenceGenerator(sequenceName = "messages_id_seq", name = "messages_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Embedded
    private MessageContent content;

    @Temporal(TemporalType.DATE)
    @Column(name = "publish_date", insertable = false)
    private Date publishDate;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "seen")
    private Boolean seen;

    @Column(name = "i_to_e")
    private Boolean direction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User investor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    @Column(name = "sender_id", insertable = false, updatable = false)
    private int sender_id;

    @Column(name = "receiver_id", insertable = false, updatable = false)
    private int receiver_id;

    @Column(name = "project_id", insertable = false, updatable = false)
    private int project_id;

    /** Protected */ Message() {
        /** For hibernate only */
    }

    public Message(MessageContent content, User owner, User investor, Project project, boolean direction) {
        this.content = content;
        this.owner = owner;
        this.investor = investor;
        this.project = project;
        this.seen = false;
        this.direction = direction;
    }


    /** Getters and setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getInvestor() {
        return investor;
    }

    public void setInvestor(User investor) {
        this.investor = investor;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content=" + content +
                ", publishDate=" + publishDate +
                ", accepted=" + accepted +
                ", seen=" + seen +
                ", investor_to_entrep=" + direction +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", project_id=" + project_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    /**
     * Model of the message content.
     * It suits for the negotiation between investors and entrepreneurs.
     */
    @Embeddable
    public static class MessageContent {

        @Column(name = "content_message", length = 250)
        private String comment;

        @Column(name = "content_offer", length = 100, nullable = false)
        private String offer;

        @Column(name = "content_interest", length = 100)
        private String interest;

        /** Protected */ MessageContent() {
            /** For hibernate only */
        }

        public MessageContent(String comment, String offer, String interest) {
            this.comment = comment;
            this.offer = offer;
            this.interest = interest;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        @Override
        public String toString() {
            return "MessageContent{" +
                    "comment='" + comment + '\'' +
                    ", offer='" + offer + '\'' +
                    ", interest='" + interest + '\'' +
                    '}';
        }
    }
}
