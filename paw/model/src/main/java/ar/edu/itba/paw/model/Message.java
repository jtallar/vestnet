package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User receiver;

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

    public Message(MessageContent content, User sender, User receiver, Project project) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.project = project;
    }

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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
        private String message;

        @Column(name = "content_offer", length = 100, nullable = false)
        private String offer;

        @Column(name = "content_interest", length = 100)
        private String interest;

        /** Protected */ MessageContent() {
            /** For hibernate only */
        }

        public MessageContent(String message, String offer, String interest) {
            this.message = message;
            this.offer = offer;
            this.interest = interest;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
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
                    "message='" + message + '\'' +
                    ", offer='" + offer + '\'' +
                    ", interest='" + interest + '\'' +
                    '}';
        }
    }
}
