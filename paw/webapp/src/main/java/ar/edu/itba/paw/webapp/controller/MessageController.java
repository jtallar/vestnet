package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MessageService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    /**
     * Session user data.
     * @return The logged in user.
     */
    @ModelAttribute("sessionUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("\n\n loggedUser() called\n\n");
        if(auth != null)
            return userService.findByUsername(auth.getName()).orElse(null);
        return null;
    }

    /**
     * Gets all the unread messages for a project.
     * @param projectId The unique project id.
     * @return List of all messages.
     */
    @RequestMapping(value = "/message/{project_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> unreadMessages(@PathVariable("project_id") long projectId) {
        return messageService.getProjectUnread(loggedUser().getId(), projectId);
    }

    /**
     * Updates the status to accepted for a specific message.
     * @param projectId The unique project id.
     * @param senderId The unique user message sender id.
     * @return Model and view.
     */
    @RequestMapping(value = "/message/accept/{project_id}/{sender_id}")
    public ModelAndView acceptMessage(@PathVariable("project_id") long projectId, @PathVariable("sender_id") long senderId ){
        messageService.updateMessageStatus(senderId, loggedUser().getId(), projectId, true);
        return new ModelAndView("redirect:/messages");
    }

    /**
     * Updates the status to rejected for a specific message.
     * @param projectId The unique project id.
     * @param senderId The unique user message sender id.
     * @return Model and view.
     */
    @RequestMapping(value = "/message/refuse/{project_id}/{sender_id}")
    public ModelAndView refuseMessage(@PathVariable("project_id") long projectId, @PathVariable("sender_id") long senderId ){
        messageService.updateMessageStatus(senderId, loggedUser().getId(), projectId, false);
        return new ModelAndView("redirect:/messages");
    }
}
