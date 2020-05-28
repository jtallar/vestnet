package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.MessageService;
import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return messageService.getProjectUnread(sessionUser.getId(), projectId);
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
        System.out.println("MATIII: " + request.getRequestURL());
        System.out.println("MATIII: " + request.getContextPath());
        System.out.println("MATIII: " + request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath());

        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
//        User senderUser = userService.findById(senderId).orElseThrow(MessagingException::new);
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
        // TODO also check this

//        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
//        User senderUser = userService.findById(senderId).orElseThrow(MessagingException::new);
//        emailService.sendEmailAnswer(loggedUser, false, senderUser.getEmail(),
//                projectId, baseUrl, senderUser.getLocation().getCountry().getLocale());
////        return new ModelAndView("redirect:/messages#dashboard-project-" + projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/deals")
    public ModelAndView deals(@RequestParam(name = "page", defaultValue = "1") String page){
        final ModelAndView mav = new ModelAndView("user/deals");
        long id = sessionUser.getId();
        boolean hasNext = messageService.hasNextDeal(page, id);
        mav.addObject("hasNext", hasNext);
        List<Message> messages = messageService.getAccepted(id,page, messageService.getPageSize());
        mav.addObject("page", page);
        mav.addObject("messages", messages);

        return mav;
    }


    @RequestMapping("/requests")
    public ModelAndView requests(@RequestParam(name = "page", defaultValue = "1")String page){
        final ModelAndView mav = new ModelAndView("user/requests");
        long id = sessionUser.getId();
        boolean hasNext = messageService.hasNextRequest(page, id);
        List<Message> messages = messageService.getOffersDone(id,page, messageService.getPageSize());
        mav.addObject("hasNext", hasNext);
        mav.addObject("page", page);
        mav.addObject("messages", messages);

        return mav;

    }
}
