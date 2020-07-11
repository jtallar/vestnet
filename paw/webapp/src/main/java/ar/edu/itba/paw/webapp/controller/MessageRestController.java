package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.webapp.dto.OfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

@Path("messages")
@Component
public class MessageRestController {

    @Autowired
    private MessageService messageService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(final OfferDto offerDto) {
        final Message message = messageService.create(offerDto.getBody(), offerDto.getOffers(), offerDto.getExchange(),
                offerDto.getSenderId(), offerDto.getReceiverId(), offerDto.getProjectId(), uriInfo.getBaseUri());
        return Response.created(uriInfo.getAbsolutePath()).build(); // TODO what do we do here?
    }
}
