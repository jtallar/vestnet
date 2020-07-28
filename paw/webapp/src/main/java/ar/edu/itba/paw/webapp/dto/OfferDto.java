package ar.edu.itba.paw.webapp.dto;

public class OfferDto {

    private String body;
    private int offers;
    private String exchange;
    private long senderId;
    private long receiverId;
    private long projectId;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getOffers() {
        return offers;
    }

    public void setOffers(int offers) {
        this.offers = offers;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
