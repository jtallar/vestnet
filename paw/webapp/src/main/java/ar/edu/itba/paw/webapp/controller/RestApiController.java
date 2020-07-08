package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.State;
import ar.edu.itba.paw.webapp.event.OfferAnswerEvent;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    /**
     * Puts a project as a favorite to an user.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     * @return Response entity.
     */
    @RequestMapping(value = "/addFavorite", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> addFavorite(@RequestParam("p_id") Long projectId,
                                               @RequestParam("u_id") Long userId) {

        userService.addFavorite(userId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Removes a project as a favorite to an user.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     * @return Response entity.
     */
    @RequestMapping(value = "/deleteFavorite", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> deleteFavorite(@RequestParam("p_id") Long projectId,
                                                  @RequestParam("u_id") Long userId) {

        userService.deleteFavorite(userId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Gets all the unread messages for a project.
     * @param projectId The unique project id.
     * @param userId The user id that requested
     * @return List of all messages.
     */
    @RequestMapping(value = "/messages/unread",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> unreadMessages(@RequestParam(name = "p_id") Long projectId,
                                        @RequestParam(name = "u_id") Long userId) {

        List<Message> messages = userService.getProjectUnreadMessages(userId, projectId);
        messages.stream().forEach(message -> {
            message.setProject(null);
            message.setReceiver(null);
            message.setSender(null); });
        return messages;
    }


    /**
     * Updates the status to accepted for a specific message.
     * @param projectId The unique project id.
     * @param senderId The unique user message sender id.
     * @param receiverId The unique receiver id. Session user.
     * @param value The value to update status
     * @return Model and view.
     */
    @RequestMapping(value = "/message/update", method = RequestMethod.PUT, headers = "accept=application/json")
    @ResponseBody
    public ResponseEntity<Boolean> acceptMessage(@RequestParam(name = "p_id") Long projectId,
                                                 @RequestParam(name = "s_id") Long senderId,
                                                 @RequestParam(name = "r_id") Long receiverId,
                                                 @RequestParam(name = "val") Boolean value,
                                                 HttpServletRequest request) {

        User sender = userService.findById(senderId).orElseThrow(UserNotFoundException::new);
        User receiver = userService.findById(receiverId).orElseThrow(UserNotFoundException::new);
        Project project = projectService.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        messageService.updateMessageStatus(receiver.getId(), sender.getId(), project.getId(), value);
        eventPublisher.publishEvent(new OfferAnswerEvent(sender, receiver, project, value, getBaseUrl(request)));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /** Auxiliary functions */

    /**
     * Creates the base url needed.
     * @param request The given request to get the base url from.
     * @return Base url string formatted.
     */
    private String getBaseUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath()));
    }
}
