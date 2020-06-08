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
    private ApplicationEventPublisher eventPublisher;


    /**
     * Gets the stored image for a project.
     * @param projectId The unique project id.
     * @return The image in a byte vector.
     */
    @RequestMapping(value = "/imageController/project/{p_id}", produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] imageControllerProject(@PathVariable("p_id") long projectId) {

        return projectService.getPortraitImage(projectId);
    }


    /**
     * Gets the stored image for a user.
     * @param imageId The unique user image id.
     * @return The image in a byte vector.
     */
    @RequestMapping(value = "/imageController/user/{i_id}")
    @ResponseBody
    public byte[] imageControllerUser(@PathVariable("i_id") Long imageId) {

        return userService.getProfileImage(imageId);
    }


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
        eventPublisher.publishEvent(new OfferAnswerEvent(sender, receiver, project, value, getBaseUrl(request)));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Adds a hit to a given project.
     * @param projectId The unique project id.
     * @return Response entity.
     */
    @RequestMapping(value = "/addHit/{p_id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> addHit(@PathVariable("p_id") long projectId) {
        projectService.addHit(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Gets all the countries stored in the database.
     * @return List of all the countries.
     */
    @RequestMapping(value = "/location/country",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Country> countryList() {
        return locationService.findAllCountries();
    }


    /**
     * Gets all the states for a given country.
     * @param countryId The unique country id.
     * @return List of all the available country's states.
     */
    @RequestMapping(value = "/location/state/{country_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<State> stateList(@PathVariable("country_id") int countryId) {
        return locationService.findStates(countryId);
    }


    /**
     * Gets all the cites for a given state.
     * @param stateId The unique state id.
     * @return List of all the available state's cities.
     */
    @RequestMapping(value = "/location/city/{state_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<City> cityList(@PathVariable("state_id") int stateId) {
        return locationService.findCities(stateId);
    }


    /** Auxiliary functions */

    /**
     * Creates the base url needed.
     * @param request The given request to get the base url from.
     * @return Base url string formatted.
     */
    private String getBaseUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
    }
}
