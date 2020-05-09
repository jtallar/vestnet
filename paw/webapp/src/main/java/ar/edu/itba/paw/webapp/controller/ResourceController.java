package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class ResourceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/imageController/project/{p_id}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] imageControllerProject(@PathVariable("p_id") long id) {
        // Si no tiene pic --> Devuelve null
        // TODO: CHANGE NO IMAGE PIC
        byte[] image = projectService.findImageForProject(id);
        if (image == null) {
            try {
                Resource stockImage = new ClassPathResource("projectNoImage.png");
                image = IOUtils.toByteArray(stockImage.getInputStream());
            } catch (IOException e) {
                LOGGER.debug("Could not load stock image");
            }
        }
        return image;
    }

    @RequestMapping(value = "/imageController/user/{u_id}")
    @ResponseBody
    public byte[] imageControllerUser(@PathVariable("u_id") long id) {
        // Si no tiene pic --> Devuelve null
        // TODO: CHANGE NO IMAGE PIC
        byte[] image = userService.findImageForUser(id);
        if (image == null) {
            try {
                Resource stockImage = new ClassPathResource("userNoImage.png");
                image = IOUtils.toByteArray(stockImage.getInputStream());
            } catch (IOException e) {
                LOGGER.debug("Could not load stock image. Error {}", e.getMessage());
            }
        }
        return image;
    }
}
