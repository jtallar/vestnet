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

    private boolean seen;

    private boolean direction;

    private long investorId, ownerId, projectId;

    private URI investor, owner, project;

    public static OfferDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferDto offerDto = new OfferDto();

        offerDto.body = message.getContent().getComment();
        offerDto.offers = message.getContent().getOffer();
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


    public boolean isSeen() {
        return seen;
    }

    public boolean isDirection() {
        return direction;
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
