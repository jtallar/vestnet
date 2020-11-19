package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import ar.edu.itba.paw.webapp.dto.OfferDto;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("messages")
@Component
public class MessageRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    private static final int PAGE_SIZE = 3; // TODO it's okay the offerDto, idk


    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(final OfferDto offerDto) {
        final Message message = messageService.create(offerDto.getBody(), offerDto.getOffers(), offerDto.getExchange(),
                offerDto.getSenderId() /*sessionUser.getId()*/, offerDto.getReceiverId(), offerDto.getProjectId(), uriInfo.getBaseUri());
        return Response.created(uriInfo.getAbsolutePath()).build(); // TODO what do we do here?
    }

    @PUT
    @Path("/status")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response status(final OfferDto offerDto,
                           @QueryParam("accepted") @DefaultValue("true") boolean accepted) {
        Optional<Message> updatedMessage = messageService.updateMessageStatus(offerDto.getReceiverId(), offerDto.getSenderId(),
                offerDto.getProjectId(), accepted, uriInfo.getBaseUri());
        return updatedMessage.map(message -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }

    @GET
    @Path("/unread/{project_id}/{user_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response unread(@PathParam("project_id") final long projectId,
                           @PathParam("user_id") final long userId,
                           @QueryParam("last") @DefaultValue("false") boolean last) {
        List<Message> messages = new ArrayList<>();

        if (last) userService.getLastProjectOfferMessage(userId /*sessionUser.getId()*/, projectId).ifPresent(messages::add);
        else messages = userService.getProjectUnreadMessages(userId /*sessionUser.getId()*/, projectId);

        List<OfferDto> offers = messages.stream().map(m -> OfferDto.fromMessage(m, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<OfferDto>>(offers) {}).build();
    }

    @GET
    @Path("/{user_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response accepted(@PathParam("user_id") final long userId,
                             @QueryParam("offers") @DefaultValue("false") boolean offers,
                             @QueryParam("p") @DefaultValue("1") int page) {
        Page<Message> messagePage;
        if (offers) messagePage = userService.getOfferMessages(userId /*sessionUser.getId()*/, page, PAGE_SIZE);
        else messagePage = userService.getAcceptedMessages(userId /*sessionUser.getId()*/, page, PAGE_SIZE);

        List<OfferDto> messages = messagePage.getContent().stream().map(m -> OfferDto.fromMessage(m, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getEndPage()).build(), "last")
                .build();
    }
}
