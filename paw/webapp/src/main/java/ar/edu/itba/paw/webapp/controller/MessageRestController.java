package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.webapp.component.SessionUser;
import ar.edu.itba.paw.webapp.dto.OfferDto;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionUser sessionUser;

    @Context
    private UriInfo uriInfo;

    private static final int PAGE_SIZE = 5;

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    public Response offer(@Valid final OfferDto offerDto) {
        if (sessionUser.isAnonymous()) return Response.status(Response.Status.FORBIDDEN).build();

        final Optional<Message> message = messageService.create(OfferDto.toMessage(offerDto), sessionUser.getId(), uriInfo.getBaseUri());

        return message.map(m -> Response.created(uriInfo.getAbsolutePath()).build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
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
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getEndPage()).build(), "last")
                .build();
    }

    @GET
    @Path("/investor")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response investorChats(@QueryParam("a") @DefaultValue("false") boolean accepted,
                                  @QueryParam("p") @DefaultValue("1") int page) {

        final Page<Message> messagePage = messageService.getInvestorProjects(sessionUser.getId(), accepted, page, PAGE_SIZE);

        List<OfferDto> messages = messagePage.getContent().stream().map(p -> OfferDto.fromMessage(p, uriInfo)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<OfferDto>>(messages) {})
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getEndPage()).build(), "last")
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
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getStartPage()).build(), "first")
                .link(uriInfo.getRequestUriBuilder().queryParam("p", messagePage.getEndPage()).build(), "last")
                .build();
    }

    @PUT
    @Path("/status/{project_id}/{investor_id}")
    public Response status(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId,
                           @QueryParam("p") @DefaultValue("false") boolean accepted) {

        final Optional<Message> updatedMessage = messageService.updateMessageStatus(projectId, investorId, sessionUser.getId(), accepted, uriInfo.getBaseUri());

        return updatedMessage.map(message -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }


    @PUT
    @Path("/seen/{project_id}/{investor_id}")
    public Response seen(@PathParam("project_id") final long projectId,
                           @PathParam("investor_id") final long investorId) {

        final Optional<Message> updatedMessage = messageService.updateMessageSeen(projectId, investorId, sessionUser.getId(), uriInfo.getBaseUri());

        return updatedMessage.map(message -> Response.ok().build())
                .orElse(Response.status(Response.Status.NOT_FOUND.getStatusCode()).build());
    }




    // Notification Center

    // Investor
    // TODO: GET investor notifications (session_user_id)
    // GET last messages from all different projects (investor_id) ORDERED + NOT PAGED +++++ FILTER BY NOT HIS AND NOT SEEN + COUNT (notifications)
    // Each chat entry would have its own notification based on its last message presented. The query above is for the top bar notification.

    // Entrepreneur

    // TODO: GET project Notification (project_id)
    // GET last messages from all different investors (project_id) ORDERED + NOT PAGED +++++ FILTER BY NOT HIS AND NOT SEEN + COUNT (notifications)
    // Notification for each project, inside of it, the chats would have its own notifications based on its last message presented.
    // The method above is for the notification on the dashboard for each project

    // TODO: GET entrepreneur Notifications (session_user_id)
    // the method above for each project owned
}
