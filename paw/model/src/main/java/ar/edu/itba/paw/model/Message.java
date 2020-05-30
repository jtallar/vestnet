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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_message_id_seq")
    @SequenceGenerator(sequenceName = "messages_message_id_seq", name = "messages_message_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Embedded
    private MessageContent content;

    @Temporal(TemporalType.DATE)
    @Column(name = "publish_date")
    private Date publishDate;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content=" + content +
                ", publishDate=" + publishDate +
                ", accepted=" + accepted +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", project=" + project +
                '}';
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
