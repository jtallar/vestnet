package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Message;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.OrderField;
import ar.edu.itba.paw.model.components.Page;
import ar.edu.itba.paw.model.components.SearchField;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.event.OfferEvent;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.FilterForm;
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
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
     * The model and view mapping for the feed.
     * @param form Filter form.
     * @param error Error bind to the filter form.
     * @param page The paged asked to show.
     * @return The model and view
     */
    @RequestMapping(value = "/projects", method = {RequestMethod.GET})
    public ModelAndView mainView(@Valid @ModelAttribute("filter") FilterForm form,
                                 final BindingResult error,
                                @RequestParam(name = "page", defaultValue = "1") Integer page) {

        Page<Project> projectPage = projectService.findAll(form.getFiltersMap(), form.getOrder(), page, PAGE_SIZE);
        projectPage.setPageRange(PAGINATION_ITEMS);
        final ModelAndView mav = new ModelAndView("project/feed");
        mav.addObject("projectPage", projectPage);
        mav.addObject("categories", projectService.getAllCategories());
        mav.addObject("fieldValues", SearchField.values());
        mav.addObject("orderValues", OrderField.values());
        if (sessionUser.isInvestor())
            mav.addObject("user", userService.findById(sessionUser.getId()).orElseThrow(UserNotFoundException::new));
        return mav;
    }


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


    /**
     * Shows the new project form.
     * @param newProjectFields Form fields to be filled for a new project.
     * @return Model and view.
     */
    @RequestMapping(value = "/newProject", method = {RequestMethod.GET})
    public ModelAndView createProject(@ModelAttribute("newProjectForm") final NewProjectFields newProjectFields) {

        final ModelAndView mav = new ModelAndView("project/newProject");
        mav.addObject("categories", projectService.getAllCategories());
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        mav.addObject("maxSlideshowCount", WebConfig.MAX_SLIDESHOW_COUNT);
        return mav;
    }


    /**
     * New project post mapping. On submit.
     * @param projectFields The filled form fields for new project.
     * @param errors Errors on the form fields.
     * @return Model and view.
     */
    @RequestMapping(value = "/newProject", method = {RequestMethod.POST})
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields,
                                      final BindingResult errors) {

        if(errors.hasErrors()) return logFormErrorsAndReturn(errors, "New Project", createProject(projectFields));

        Project newProject;
        try {
            List<byte[]> slideshow = projectFields.getSlideshowImages().stream().map(i -> {
                try { return i.getBytes(); } catch (IOException e) {} return new byte[0]; }).collect(Collectors.toList());

            newProject = projectService.create(projectFields.getTitle(), projectFields.getSummary(),
                    projectFields.getCost(), sessionUser.getId(), projectFields.getCategories(),
                    projectFields.getPortraitImage().getBytes(), slideshow);
        } catch (IOException e) {
            LOGGER.error("Error {} when getting bytes from MultipartFile", e.getMessage());
            return createProject(projectFields);
        }

        return new ModelAndView("redirect:/dashboard#dashboard-project-" + newProject.getId());
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
