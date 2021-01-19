package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import ar.edu.itba.paw.webapp.dto.offer.OfferDto;
import ar.edu.itba.paw.webapp.dto.offer.OfferInvestorDto;
import ar.edu.itba.paw.webapp.dto.offer.OfferProjectDto;
import ar.edu.itba.paw.webapp.dto.offer.OfferStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("messages")
@Component
public class MessageRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRestController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionUserFacade sessionUser;

    @Context
    private UriInfo uriInfo;

    // TODO: Puedo poner PAGE_SIZE en 6 para el /investor? Se muestran en filas de 3 columnas
    private static final int PAGE_SIZE = 5;

    @POST
    @Path("/{project_id}/{investor_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(@PathParam("project_id") final long projectId,
                          @PathParam("investor_id") final long investorId,
                          @Valid final OfferDto offerDto) {

        final Optional<Message> message = messageService.create(projectId, investorId, sessionUser.getId(),
                OfferDto.toMessageContent(offerDto), offerDto.getExpiryDays(), uriInfo.getBaseUri());

        return message.map(m -> Response.created(uriInfo.getAbsolutePath()).header("Access-Control-Expose-Headers", "Location").build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }


    @POST
    @Path("/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(@PathParam("project_id") final long projectId,
                          @Valid final OfferDto offerDto) {

        return offer(projectId, sessionUser.getId(), offerDto);
    }


    @GET
    @Path("/project/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectChats(@PathParam("project_id") final long projectId,
                                    @QueryParam("a") @DefaultValue("false") boolean accepted,
                                    @QueryParam("p") @DefaultValue("1") int page) {

        final Page<Message> messagePage = messageService.getProjectInvestors(projectId, sessionUser.getId(), accepted, page, PAGE_SIZE);

        List<OfferInvestorDto> messages = messagePage.getContent().stream().map(p -> OfferInvestorDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferInvestorDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getNextPage()).build(), "next")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }

    // TODO: Es muy costoso agregar el nombre del proyecto, no? Me falta el nombre y el link de la portrait (de tener)
    // TODO: Deberia devolverme metadata ademas de la lista de OfferDto, con datos como Sumatoria de plata invertida
    @GET
    @Path("/investor")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response investorChats(@QueryParam("a") @DefaultValue("false") boolean accepted,
                                  @QueryParam("p") @DefaultValue("1") int page) {

        final Page<Message> messagePage = messageService.getInvestorProjects(sessionUser.getId(), accepted, page, PAGE_SIZE);

        List<OfferProjectDto> messages = messagePage.getContent().stream().map(p -> OfferProjectDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferProjectDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", 1).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getStartPage()).build(), "start")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getEndPage()).build(), "end")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getTotalPages()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }


    @GET
    @Path("/chat/{project_id}/{investor_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response chat(@PathParam("project_id") final long projectId,
                         @PathParam("investor_id") final long investorId,
                         @QueryParam("p") @DefaultValue("1") int page) {

        final Page<Message> messagePage = messageService.getConversation(projectId, investorId, sessionUser.getId(), page, PAGE_SIZE);

        List<OfferDto> messages = messagePage.getContent().stream().map(p -> OfferDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getNextPage()).build(), "next")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }


    @GET
    @Path("/chat/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response chat(@PathParam("project_id") final long projectId,
                         @QueryParam("p") @DefaultValue("1") int page) {
        return chat(projectId, sessionUser.getId(), page);
    }


    // TODO: Deberiamos aca marcar como "unseen" o de alguna manera para que le llegue notificacion al otro?
    @PUT
    @Path("/status/{project_id}/{investor_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response status(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId,
                           final OfferStatusDto offerStatusDto) {

        final Optional<Message> updatedMessage = messageService.updateMessageStatus(projectId, investorId, sessionUser.getId(), offerStatusDto.isAccepted(), uriInfo.getBaseUri());

        return updatedMessage.map(message -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/status/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response status(@PathParam("project_id") final long projectId,
                           final OfferStatusDto offerStatusDto) {
        return status(projectId, sessionUser.getId(), offerStatusDto);
    }


    // TODO: Estamos marcando como visto solamente al ultimo mensaje. Que pasa si un mensaje expiro? Jamas se le va el visto.
    //  O deberiamos filtrarlos en los demas lugares? Eg: en notificationCount, decir que este unseen y unexpired. Y lo mismo en el front.
    @PUT
    @Path("/seen/{project_id}/{investor_id}")
    public Response seen(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId) {

        final Optional<Message> updatedMessage = messageService.updateMessageSeen(projectId, investorId, sessionUser.getId(), uriInfo.getBaseUri());

        return updatedMessage.map(message -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/seen/{project_id}")
    public Response seen(@PathParam("project_id") final long projectId) {
        return seen(projectId, sessionUser.getId());
    }


    /** Notifications */

    @GET
    @Path("/notifications/project/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectNotifications(@PathParam("project_id") final long projectId) {

        long count = messageService.projectNotifications(projectId, sessionUser.getId());
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }


    @GET
    @Path("/notifications")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectNotifications() {

        long count = messageService.userNotifications(sessionUser.getId(), sessionUser.isInvestor());
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }
}
