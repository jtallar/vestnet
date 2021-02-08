package ar.edu.itba.paw.webapp.dto.offer;

import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.image.ProjectImage;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OfferProjectDto extends OfferDto {

    @Size(min = 5, max = 50)
    @NotBlank
    private String projectName;

    private boolean projectPortraitExists;

    private URI projectPortraitImage;


    public static OfferProjectDto fromMessage(Message message, UriInfo uriInfo) {
        final OfferProjectDto offerDto = new OfferProjectDto();

        offerDto.setId(message.getId());

        offerDto.setComment(message.getContent().getComment());
        offerDto.setOffer(message.getContent().getOffer());
        offerDto.setExchange(message.getContent().getInterest());

        offerDto.setPublishDate(message.getPublishDate());
        offerDto.setExpiryDate(message.getExpiryDate());

        offerDto.setSeen(message.isSeen());
        offerDto.setSeenAnswer(message.isSeenAnswer());
        offerDto.setDirection(message.getDirection());
        offerDto.setAccepted(message.getAccepted());

        offerDto.setInvestor(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getInvestorId())).build());
        offerDto.setOwner(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(message.getOwnerId())).build());
        offerDto.setProject(uriInfo.getBaseUriBuilder().path("projects").path(String.valueOf(message.getProjectId())).build());
        offerDto.setChat(uriInfo.getBaseUriBuilder().path("messages").path("chat").path(String.valueOf(message.getProjectId())).path(String.valueOf(message.getInvestorId())).build());

        offerDto.setInvestorId(message.getInvestorId());
        offerDto.setOwnerId(message.getOwnerId());
        offerDto.setProjectId(message.getProjectId());
        offerDto.setProjectName(message.getProject().getName());

        offerDto.setProjectPortraitExists(message.getProject().hasPortraitImage());
        if (message.getProject().hasPortraitImage()) offerDto.setProjectPortraitImage(uriInfo.getBaseUriBuilder().path("/images/projects").path(String.valueOf(message.getProjectId())).build());

        return offerDto;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isProjectPortraitExists() {
        return projectPortraitExists;
    }

    public void setProjectPortraitExists(boolean projectPortraitExists) {
        this.projectPortraitExists = projectPortraitExists;
    }

    public URI getProjectPortraitImage() {
        return projectPortraitImage;
    }

    public void setProjectPortraitImage(URI projectPortraitImage) {
        this.projectPortraitImage = projectPortraitImage;
    }
}
