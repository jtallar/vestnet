package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Size;

public class MailFields {

    @Size(max = 250)
    private String body;

    private int offers;

    @Size(max = 100)
    private String exchange;

    /** Injected */
    private long receiverId;


    /** Getters and setters */

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }
}
