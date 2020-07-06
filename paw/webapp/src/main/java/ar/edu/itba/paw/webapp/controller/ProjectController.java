package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.event.OfferEvent;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    /** This are constant for now */
    private static final int PAGE_SIZE = 12;
    private static final int PAGINATION_ITEMS = 5;


    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    protected SessionUserFacade sessionUser;



    /**
     * Single project view page.
     * @param mailFields Fields for mail contact
     * @param id The unique project id.
     * @param sent Boolean if mail was sent.
     * @return Model and view.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.GET})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields,
                                          @PathVariable("id") long id,
                                          @RequestParam(name = "sent", defaultValue = "false") boolean sent) {

        final ModelAndView mav = new ModelAndView("project/singleView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("sent", sent);
        if (sessionUser.isInvestor()) {
            mav.addObject("user", userService.findById(sessionUser.getId()).orElseThrow(UserNotFoundException::new));
            mav.addObject("lastMessage", userService.getLastProjectOfferMessage(sessionUser.getId(), id));
        }
        return mav;
    }


    /**
     * Post method. Used when contacted project owner.
     *
     * @param mailFields Fields for mail contact
     * @param errors Errors on form.
     * @param projectId The unique project id.
     * @return Model and view.
     * @throws MessagingException When mail cannot be sent.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.POST})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields,
                                          @PathVariable("id") Long projectId,
                                          final BindingResult errors,
                                          HttpServletRequest request) {

        if(errors.hasErrors()) return logFormErrorsAndReturn(errors, "Message", singleProjectView(mailFields, projectId, false));

        User sender = userService.findById(sessionUser.getId()).orElseThrow(UserNotFoundException::new);
        User receiver = userService.findById(mailFields.getReceiverId()).orElseThrow(UserNotFoundException::new);
        Project project = projectService.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        Message message = messageService.create(mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(), sender.getId(), receiver.getId(), projectId);
        eventPublisher.publishEvent(new OfferEvent(sender, receiver, project, message, getBaseUrl(request)));
        return new ModelAndView("redirect:/projects/{id}" + "?sent=true");
    }





    /** Auxiliary functions */

    /**
     * Logs form errors and returns the given model and view
     * @param errors The errors returned.
     * @param formName The form name used to generate the string.
     * @param modelAndView Model and view to return to.
     * @return To the given model and view.
     */
    private ModelAndView logFormErrorsAndReturn(BindingResult errors, String formName, ModelAndView modelAndView) {
        LOGGER.error(formName + " failed. There are {} errors in form\n", errors.getErrorCount());
        for (ObjectError error : errors.getAllErrors())
            LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
        return modelAndView;
    }


    /**
     * Creates the base url needed.
     * @param request The given request to get the base url from.
     * @return Base url string formatted.
     */
    private String getBaseUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath()));
    }


}
