package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Message;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class OfferDto {

    @Size(max = 250)
    @NotBlank
    private String body;

    @Size(max = 100)
    @NotBlank
    private String offers;

    @Size(max = 100)
    @NotBlank
    private String exchange;

    private long receiverId, senderId, projectId;

    private URI receiver, sender, project;

    public static OfferDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferDto offerDto = new OfferDto();

        offerDto.body = message.getContent().getMessage();
        offerDto.offers = message.getContent().getOffer();
        offerDto.exchange = message.getContent().getInterest();

        offerDto.receiver = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getReceiver_id())).build();
        offerDto.sender = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getSender_id())).build();
        offerDto.project = uriInfo.getAbsolutePathBuilder().path("projects").path(String.valueOf(message.getProject_id())).build();

        offerDto.receiverId = message.getReceiver_id();
        offerDto.senderId = message.getSender_id();
        offerDto.projectId = message.getProject_id();

        return offerDto;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }


    public URI getReceiver() {
        return receiver;
    }

    public void setReceiver(URI receiver) {
        this.receiver = receiver;
    }

    public URI getSender() {
        return sender;
    }

    public void setSender(URI sender) {
        this.sender = sender;
    }

    public URI getProject() {
        return project;
    }

    public void setProject(URI project) {
        this.project = project;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
