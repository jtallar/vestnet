package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class OfferDto {

    @Size(max = 250)
    @NotBlank
    private String comment;

    @Min(1000)
    @Max(1000000000)
    private long offer;

    @Size(max = 100)
    @NotBlank
    private String exchange;

    @Min(1)
    @Max(31)
    private int expiryDays;

    private Date publishDate;

    private Date expiryDate;

    private boolean seen;

    private boolean direction;

    private long investorId, ownerId, projectId;

    private URI investor, owner, project, chat;

    public static OfferDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferDto offerDto = new OfferDto();

        offerDto.comment = message.getContent().getComment();
        offerDto.offer = message.getContent().getOffer();
        offerDto.exchange = message.getContent().getInterest();

        offerDto.publishDate = message.getPublishDate();
        offerDto.expiryDate = message.getExpiryDate();

        offerDto.seen = message.getSeen();
        offerDto.direction = message.getDirection();

        offerDto.investor = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getInvestorId())).build();
        offerDto.owner = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getOwnerId())).build();
        offerDto.project = uriInfo.getAbsolutePathBuilder().path("projects").path(String.valueOf(message.getProjectId())).build();
        offerDto.chat = uriInfo.getAbsolutePathBuilder().path("messages").path("chat").path(String.valueOf(message.getProjectId())).path(String.valueOf(message.getInvestorId())).build();

        offerDto.investorId = message.getInvestorId();
        offerDto.ownerId = message.getOwnerId();
        offerDto.projectId = message.getProjectId();

        return offerDto;
    }

    public static Message toMessage(OfferDto offerDto) {
        final Message.MessageContent content = new Message.MessageContent(offerDto.getComment(), offerDto.getOffer(), offerDto.getExchange());
        return new Message(content,
                new User(offerDto.getOwnerId()),
                new User(offerDto.getInvestorId()),
                new Project(offerDto.getProjectId()),
                offerDto.getDirection(),
                offerDto.expiryDays);
    }


    public boolean isSeen() {
        return seen;
    }

    public boolean getDirection() {
        return direction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getOffer() {
        return offer;
    }

    public void setOffer(long offer) {
        this.offer = offer;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(int expiryDays) {
        this.expiryDays = expiryDays;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public URI getInvestor() {
        return investor;
    }

    public void setInvestor(URI investor) {
        this.investor = investor;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public URI getProject() {
        return project;
    }

    public void setProject(URI project) {
        this.project = project;
    }

    public URI getChat() {
        return chat;
    }

    public void setChat(URI chat) {
        this.chat = chat;
    }

    public long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(long investorId) {
        this.investorId = investorId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
