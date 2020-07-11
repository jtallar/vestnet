package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


}
