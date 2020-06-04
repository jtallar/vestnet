package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.City;
import ar.edu.itba.paw.model.Country;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.State;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    /**
     * Gets the stored image for a project.
     * @param projectId The unique project id.
     * @return The image in a byte vector.
     */
    @RequestMapping(value = "/imageController/project/{p_id}", produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] imageControllerProject(@PathVariable("p_id") long projectId) {
        // TODO its no a project service
//        byte[] image = projectService.findImageForProject(projectId);
//        if (image != null) return image;
//        try {
//            Resource stockImage = new ClassPathResource("projectNoImage.png");
//            image = IOUtils.toByteArray(stockImage.getInputStream());
//        } catch (IOException e) { LOGGER.error("Could not load stock image. Error {}", e.getMessage()); }
//        return image;
        return null;
    }

    /**
     * Gets the stored image for a user.
     * @param userId The unique user id.
     * @return The image in a byte vector.
     */
    @RequestMapping(value = "/imageController/user/{u_id}")
    @ResponseBody
    public byte[] imageControllerUser(@PathVariable("u_id") long userId) {
        // TODO its no a user service
//        byte[] image /*= userService.findImageForUser(userId)*/ = null;
//        if (image != null) return image;
//        try {
//            Resource stockImage = new ClassPathResource("userNoImage.png");
//            image = IOUtils.toByteArray(stockImage.getInputStream());
//        } catch (IOException e) { LOGGER.error("Could not load stock image. Error {}", e.getMessage()); }
//        return image;
        return null;
    }

    /**
     * Puts a project as a favorite to an user.
     * @param projectId The unique project id.
     * @param userId The unique user id.
     * @return Response entity.
     */
    @RequestMapping(value = "/addFavorite", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> addFavorite(@RequestParam("p_id") int projectId,
                                               @RequestParam("u_id") int userId) {
        // TODO this and below

//        projectService.addFavorite(projectId, userId);
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
    public ResponseEntity<Boolean> deleteFavorite(@RequestParam("p_id") int projectId,
                                                  @RequestParam("u_id") int userId) {
        // TODO is user service
//        projectService.deleteFavorite(projectId, userId);
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
    public List<State> stateList(@PathVariable("country_id") long countryId) {
        return locationService.findStates(countryId);
    }


    /**
     * Gets all the cites for a given state.
     * @param stateId The unique state id.
     * @return List of all the available state's cities.
     */
    @RequestMapping(value = "/location/city/{state_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<City> cityList(@PathVariable("state_id") long stateId) {
        return locationService.findCities(stateId);
    }
}
