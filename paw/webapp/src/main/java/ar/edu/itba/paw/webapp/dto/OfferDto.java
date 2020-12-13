package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class OfferDto {

    @Size(max = 250)
    @NotBlank
    private String comment;

    @Size(max = 100)
    @NotBlank
    private String offer; // TODO change to Integer

    @Size(max = 100)
    @NotBlank
    private String exchange;

    private boolean seen;

    private boolean direction;

    private long investorId, ownerId, projectId;

    private URI investor, owner, project;

    public static OfferDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferDto offerDto = new OfferDto();

        offerDto.comment = message.getContent().getComment();
        offerDto.offer = message.getContent().getOffer();
        offerDto.exchange = message.getContent().getInterest();

        offerDto.seen = message.getSeen();
        offerDto.direction = message.getDirection();

        offerDto.investor = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getInvestorId())).build();
        offerDto.owner = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(message.getOwnerId())).build();
        offerDto.project = uriInfo.getAbsolutePathBuilder().path("projects").path(String.valueOf(message.getProjectId())).build();

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
                offerDto.getDirection());
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

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
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
