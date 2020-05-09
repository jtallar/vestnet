package ar.edu.itba.paw.model;

import java.time.LocalDate;

public class Message {
    private final long id;

    private final MessageContent content;

    private final LocalDate publishDate;
    private Boolean accepted;

    private final long senderId;
    private final long receiverId;
    private final long projectId;

    public Message(long id, MessageContent content, LocalDate publishDate, Boolean accepted, long senderId, long receiverId, long projectId) {
        this.id = id;
        this.content = content;
        this.publishDate = publishDate;
        this.accepted = accepted;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.projectId = projectId;
    }

    public long getId() {
        return id;
    }

    public MessageContent getContent() {
        return content;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public long getProjectId() {
        return projectId;
    }

    public class MessageContent {
        private final String message;
        private final String offer;
        private final String interest;

        public MessageContent(String message, String offer, String interest) {
            this.message = message;
            this.offer = offer;
            this.interest = interest;
        }

        public String getMessage() {
            return message;
        }

        public String getOffer() {
            return offer;
        }

        public String getInterest() {
            return interest;
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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageContent=" + content +
                ", publishDate=" + publishDate +
                ", accepted=" + accepted +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
