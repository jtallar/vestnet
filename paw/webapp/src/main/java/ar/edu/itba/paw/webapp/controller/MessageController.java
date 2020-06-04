package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.MessageService;
import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    protected SessionUserFacade sessionUser;

    /**
     * Gets all the unread messages for a project.
     * @param projectId The unique project id.
     * @return List of all messages.
     */
    @RequestMapping(value = "/message/{project_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> unreadMessages(@PathVariable("project_id") long projectId) {

        return messageService.getUserProjectUnread(sessionUser.getId(), projectId);
    }

    /**
     * Updates the status to accepted for a specific message.
     * @param projectId The unique project id.
     * @param senderId The unique user message sender id.
     * @return Model and view.
     */
    @RequestMapping(value = "/message/accept/{project_id}/{sender_id}", method = RequestMethod.PUT, headers = "accept=application/json")
    @ResponseBody
    public ResponseEntity<Boolean> acceptMessage(@PathVariable("project_id") long projectId,
                                                 @PathVariable("sender_id") long senderId,
                                                 HttpServletRequest request) throws MessagingException {

        messageService.updateMessageStatus(senderId, sessionUser.getId(), projectId, true);

//        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        User senderUser = userService.findById(senderId).orElseThrow(MessagingException::new);
//        emailService.sendEmailAnswer(loggedUser, true, senderUser.getEmail(),
//                projectId, baseUrl, senderUser.getLocation().getCountry().getLocale());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates the status to rejected for a specific message.
     * @param projectId The unique project id.
     * @param senderId The unique user message sender id.
     * @return Model and view.
     */
    @RequestMapping(value = "/message/refuse/{project_id}/{sender_id}", method = RequestMethod.PUT, headers = "accept=application/json")
    @ResponseBody
    public ResponseEntity<Boolean> refuseMessage(@PathVariable("project_id") long projectId,
                                                 @PathVariable("sender_id") long senderId,
                                                 HttpServletRequest request) throws MessagingException{
        messageService.updateMessageStatus(senderId, sessionUser.getId(), projectId, false);

//        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        User senderUser = userService.findById(senderId).orElseThrow(MessagingException::new);
//        emailService.sendEmailAnswer(loggedUser, false, senderUser.getEmail(),
//                projectId, baseUrl, senderUser.getLocation().getCountry().getLocale());
////        return new ModelAndView("redirect:/messages#dashboard-project-" + projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
