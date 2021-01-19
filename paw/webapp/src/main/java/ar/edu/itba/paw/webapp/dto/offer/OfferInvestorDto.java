package ar.edu.itba.paw.webapp.dto.offer;

import ar.edu.itba.paw.model.Message;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;

public class OfferInvestorDto extends OfferDto {

    @Size(max = 250)
    @NotBlank
    private String FirstName;

    @Size(max = 250)
    @NotBlank
    private String LastName;

    public static OfferInvestorDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferInvestorDto offerDto = new OfferInvestorDto();

        offerDto.setId(message.getId());

        offerDto.setComment(message.getContent().getComment());
        offerDto.setOffer(message.getContent().getOffer());
        offerDto.setExchange(message.getContent().getInterest());

        offerDto.setPublishDate(message.getPublishDate());
        offerDto.setExpiryDate(message.getExpiryDate());

        offerDto.setSeen(message.getSeen());
        offerDto.setDirection(message.getDirection());
        offerDto.setAccepted(message.getAccepted());

        offerDto.setInvestor(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getInvestorId())).build());
        offerDto.setOwner(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getOwnerId())).build());
        offerDto.setProject(uriInfo.getBaseUriBuilder().path("projects").path(String.valueOf(message.getProjectId())).build());
        offerDto.setChat(uriInfo.getBaseUriBuilder().path("messages").path("chat").path(String.valueOf(message.getProjectId())).path(String.valueOf(message.getInvestorId())).build());

        offerDto.setInvestorId(message.getInvestorId());
        offerDto.setOwnerId(message.getOwnerId());
        offerDto.setProjectId(message.getProjectId());

        // Lazy fetching the user data
        offerDto.setFirstName(message.getInvestor().getFirstName());
        offerDto.setLastName(message.getInvestor().getLastName());

        return offerDto;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
