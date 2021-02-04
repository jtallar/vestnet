package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.InvalidMessageException;
import ar.edu.itba.paw.interfaces.exceptions.MessageDoesNotExistException;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.component.UriInfoUtils;
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

    private static final int PAGE_SIZE = 6;

    @POST
    @Path("/{project_id}/{investor_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(@PathParam("project_id") final long projectId,
                          @PathParam("investor_id") final long investorId,
                          @Valid final OfferDto offerDto) throws InvalidMessageException {

        LOGGER.debug("Endpoint POST /messages/" + projectId + "/" + investorId + " reached with " + offerDto  + " - User is " + sessionUser.getId());

        messageService.create(projectId, investorId, sessionUser.getId(), OfferDto.toMessageContent(offerDto), offerDto.getExpiryDays(), UriInfoUtils.getBaseURI(uriInfo));
        return Response.created(uriInfo.getAbsolutePath()).header("Access-Control-Expose-Headers", "Location").build();
    }


    @POST
    @Path("/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(@PathParam("project_id") final long projectId,
                          @Valid final OfferDto offerDto) throws InvalidMessageException {

        LOGGER.debug("Endpoint POST /messages/" + projectId + " reached with " + offerDto + " - User is " + sessionUser.getId());

        return offer(projectId, sessionUser.getId(), offerDto);
    }


    @GET
    @Path("/project/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectChats(@PathParam("project_id") final long projectId,
                                    @QueryParam("a") @DefaultValue("false") boolean accepted,
                                    @QueryParam("p") @DefaultValue("1") int page) {

        LOGGER.debug("Endpoint GET /messages/project/" + projectId + " reached - User is " + sessionUser.getId());

        final Page<Message> messagePage = messageService.getProjectInvestors(projectId, sessionUser.getId(), accepted, page, PAGE_SIZE);
        final List<OfferInvestorDto> messages = messagePage.getContent().stream().map(p -> OfferInvestorDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<OfferInvestorDto>>(messages) {});
        if (messagePage.hasNext()) response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getNextPage()).build(), "next");
        return response.header("Access-Control-Expose-Headers", "Link").build();

    }

    @GET
    @Path("/investor")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response investorChats(@QueryParam("a") @DefaultValue("false") boolean accepted,
                                  @QueryParam("p") @DefaultValue("1") int page) {

        LOGGER.debug("Endpoint GET /messages/investor reached - User is " + sessionUser.getId());

        final Page<Message> messagePage = messageService.getInvestorProjects(sessionUser.getId(), accepted, page, PAGE_SIZE);
        final List<OfferProjectDto> messages = messagePage.getContent().stream().map(p -> OfferProjectDto.fromMessage(p, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<OfferProjectDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", 1).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getStartPage()).build(), "start")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getEndPage()).build(), "end")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getTotalPages()).build(), "last")
                .header("Access-Control-Expose-Headers", "Link")
                .build();
    }


    @GET
    @Path("/invested")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getInvestedAmount() {

        LOGGER.debug("Endpoint GET /messages/invested reached - User is " + sessionUser.getId());

        final long count = messageService.getInvestedAmount(sessionUser.getId(), sessionUser.isInvestor());
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }

    @GET
    @Path("/invested/{investor_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getInvestedAmount(@PathParam("investor_id") final long investorId) {

        LOGGER.debug("Endpoint GET /messages/invested/" + investorId + " reached - User is " + sessionUser.getId());

        final long count = messageService.getInvestedAmount(investorId, true);
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }


    @GET
    @Path("/chat/{project_id}/{investor_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response chat(@PathParam("project_id") final long projectId,
                         @PathParam("investor_id") final long investorId,
                         @QueryParam("p") @DefaultValue("1") int page) {

        LOGGER.debug("Endpoint GET /messages/chat/" + projectId + "/" + investorId + " reached - User is " + sessionUser.getId());

        final Page<Message> messagePage = messageService.getConversation(projectId, investorId, sessionUser.getId(), page, PAGE_SIZE);
        final List<OfferDto> messages = messagePage.getContent().stream().map(p -> OfferDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<OfferDto>>(messages) {});
        if (messagePage.hasNext()) response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("p", messagePage.getNextPage()).build(), "next");
        return response.header("Access-Control-Expose-Headers", "Link").build();
    }


    @GET
    @Path("/chat/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response chat(@PathParam("project_id") final long projectId,
                         @QueryParam("p") @DefaultValue("1") int page) {

        LOGGER.debug("Endpoint GET /messages/chat/" + projectId + " reached - User is " + sessionUser.getId());

        return chat(projectId, sessionUser.getId(), page);
    }


    @PUT
    @Path("/status/{project_id}/{investor_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response status(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId,
                           final OfferStatusDto offerStatusDto) throws MessageDoesNotExistException, InvalidMessageException {

        LOGGER.debug("Endpoint PUT /messages/status/" + projectId + "/" + investorId + " reached with " + offerStatusDto.toString() + " - User is " + sessionUser.getId());

        messageService.updateMessageStatus(projectId, investorId, sessionUser.getId(), offerStatusDto.isAccepted(),
                UriInfoUtils.getBaseURI(uriInfo)).orElseThrow(MessageDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/status/{project_id}")
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response status(@PathParam("project_id") final long projectId,
                           final OfferStatusDto offerStatusDto) throws MessageDoesNotExistException, InvalidMessageException {

        LOGGER.debug("Endpoint PUT /messages/status/" + projectId + " reached with " + offerStatusDto.toString() + " - User is " + sessionUser.getId());

        return status(projectId, sessionUser.getId(), offerStatusDto);
    }


    @PUT
    @Path("/seen/{project_id}/{investor_id}")
    public Response seen(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId) throws MessageDoesNotExistException {

        LOGGER.debug("Endpoint PUT /messages/seen/" + projectId + "/" + investorId + " reached - User is " + sessionUser.getId());

        messageService.updateMessageSeen(projectId, investorId, sessionUser.getId(), UriInfoUtils.getBaseURI(uriInfo)).orElseThrow(MessageDoesNotExistException::new);
        return Response.ok().build();
    }


    @PUT
    @Path("/seen/{project_id}")
    public Response seen(@PathParam("project_id") final long projectId) throws MessageDoesNotExistException {

        LOGGER.debug("Endpoint PUT /messages/seen/" + projectId + " reached - User is " + sessionUser.getId());

        return seen(projectId, sessionUser.getId());
    }


    /** Notifications */

    @GET
    @Path("/notifications/project/{project_id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectNotifications(@PathParam("project_id") final long projectId) {

        LOGGER.debug("Endpoint GET /messages/notifications/project/" + projectId + " reached - User is " + sessionUser.getId());

        long count = messageService.projectNotifications(projectId, sessionUser.getId());
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }


    @GET
    @Path("/notifications")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response projectNotifications() {

        LOGGER.debug("Endpoint GET /messages/notifications reached - User is " + sessionUser.getId());

        long count = messageService.userNotifications(sessionUser.getId(), sessionUser.isInvestor());
        return Response.ok(NotificationDto.fromNumber(count)).build();
    }
}
