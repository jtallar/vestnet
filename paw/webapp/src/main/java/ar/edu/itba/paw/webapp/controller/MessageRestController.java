package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import ar.edu.itba.paw.webapp.dto.OfferDto;
import ar.edu.itba.paw.webapp.dto.OfferStatusDto;
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

        List<OfferDto> messages = messagePage.getContent().stream().map(p -> OfferDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getEndPage()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }

    // TODO: Este metodo que onda, para que sirve??
    @GET
    @Path("/investor")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response investorChats(@QueryParam("a") @DefaultValue("false") boolean accepted,
                                  @QueryParam("p") @DefaultValue("1") int page) {

        final Page<Message> messagePage = messageService.getInvestorProjects(sessionUser.getId(), accepted, page, PAGE_SIZE);

        List<OfferDto> messages = messagePage.getContent().stream().map(p -> OfferDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getEndPage()).build(), "last")
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
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getEndPage()).build(), "last")
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
