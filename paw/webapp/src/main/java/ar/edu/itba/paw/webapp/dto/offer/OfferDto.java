package ar.edu.itba.paw.webapp.dto.offer;

import ar.edu.itba.paw.model.Message;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class OfferDto {
    private long id;

    @Size(max = 250)
    @NotBlank
    private String comment;

    @Min(100)
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

    private boolean seenAnswer;

    private boolean direction;

    private Boolean accepted;

    private long investorId, ownerId, projectId;

    private URI investor, owner, project, chat;

    public static OfferDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferDto offerDto = new OfferDto();

        offerDto.id = message.getId();

        offerDto.comment = message.getContent().getComment();
        offerDto.offer = message.getContent().getOffer();
        offerDto.exchange = message.getContent().getInterest();

        offerDto.publishDate = message.getPublishDate();
        offerDto.expiryDate = message.getExpiryDate();

        offerDto.seen = message.isSeen();
        offerDto.seenAnswer = message.isSeenAnswer();
        offerDto.direction = message.getDirection();
        offerDto.accepted = message.getAccepted();

        offerDto.investor = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getInvestorId())).build();
        offerDto.owner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getOwnerId())).build();
        offerDto.project = uriInfo.getBaseUriBuilder().path("projects").path(String.valueOf(message.getProjectId())).build();
        offerDto.chat = uriInfo.getBaseUriBuilder().path("messages").path("chat").path(String.valueOf(message.getProjectId())).path(String.valueOf(message.getInvestorId())).build();

        offerDto.investorId = message.getInvestorId();
        offerDto.ownerId = message.getOwnerId();
        offerDto.projectId = message.getProjectId();

        return offerDto;
    }

    public static Message.MessageContent toMessageContent(OfferDto offerDto) {
        return new Message.MessageContent(offerDto.getComment(), offerDto.getOffer(), offerDto.getExchange());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isSeenAnswer() {
        return seenAnswer;
    }

    public void setSeenAnswer(boolean seenAnswer) {
        this.seenAnswer = seenAnswer;
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

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
