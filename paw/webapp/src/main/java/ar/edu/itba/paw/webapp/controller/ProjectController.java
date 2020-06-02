package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.MessageAlreadySentException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.components.SearchField;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.FilterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    protected SessionUserFacade sessionUser;





    /**
     * Message not set exception handler
     * @return Model and view.
     */
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView failedEmail() {
        return new ModelAndView("error/error");
    }




    /**
     */
    @RequestMapping(value = "/projects", method = {RequestMethod.GET})
    public ModelAndView mainView(@Valid @ModelAttribute("filter") FilterForm form,
                                 final BindingResult error,
                                @RequestParam(name = "page", defaultValue = "1") Integer page) {

        System.out.println("MATIIII: " + form.toString());

        List<Project> projects = projectService.findFiltered(form.getFiltersMap());

        final ModelAndView mav = new ModelAndView("project/feed");
        mav.addObject("categories", categoriesService.findAll());
        mav.addObject("fields", SearchField.values());
        mav.addObject("projects", projects);
        mav.addObject("startPage", 1);
        mav.addObject("endPage",1);
        mav.addObject("page", 1);
//
//        if (sessionUser.isInvestor())
//            mav.addObject("isFav", projectService.isFavorite(projects.stream().map(Project::getId).collect(Collectors.toList()), sessionUser.getId()));
//        else
            mav.addObject("isFav", new ArrayList<>());
        return mav;
    }

    /**
     * Single project view page.
     *
     * @param mailFields    Fields for mail contact
     * @param id            The unique project id.
     * @param contactStatus Boolean if mail was sent.
     * @return Model and view.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.GET})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields,
                                          @PathVariable("id") long id,
                                          @RequestParam(name = "contactStatus", defaultValue = "0") int contactStatus) {

        final ModelAndView mav = new ModelAndView("project/singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("isFav", projectService.isFavorite(id, sessionUser.getId()));
        mav.addObject("contactStatus", contactStatus);
        return mav;
    }


    /**
     * Post method. Used when contacted project owner.
     *
     * @param mailFields Fields for mail contact
     * @param errors     Errors on form.
     * @param id         The unique project id.
     * @return Model and view.
     * @throws MessagingException When mail cannot be sent.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.POST})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields,
                                          @PathVariable("id") long id,
                                          final BindingResult errors,
                                          HttpServletRequest request) throws MessagingException {
        if (errors.hasErrors()) {
            LOGGER.error("Contact failed. There are {} errors in form\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            return singleProjectView(mailFields, id, 0);
        }

        try {
            messageService.create(mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(),
                    sessionUser.getId(), mailFields.getToId(), id);
        } catch (MessageAlreadySentException e) {
            LOGGER.error("Message already sent to this user about this project.");
            return singleProjectView(mailFields, id, 2);
        }

//        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
//        LOGGER.debug("\n\nLocale: {}\n\n", mailFields.getLocale());
// TODO do not forget this

//        emailService.sendNewEmail(userService.gun, mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(),
//                mailFields.getTo(), mailFields.getProject(), id, baseUrl, mailFields.getLocale());
        return new ModelAndView("redirect:/projects/{id}?contactStatus=1");
    }

    /**
     * Shows the new project form.
     *
     * @param newProjectFields Form fields to be filled for a new project.
     * @return Model and view.
     */
    @RequestMapping(value = "/newProject", method = {RequestMethod.GET})
    public ModelAndView createProject(@ModelAttribute("newProjectForm") final NewProjectFields newProjectFields) {
        final ModelAndView mav = new ModelAndView("project/newProject");
        mav.addObject("categories", categoriesService.findAll());
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        return mav;
    }


    /**
     * New project post mapping. On submit.
     *
     * @param projectFields The filled form fields for new project.
     * @param errors        Errors on the form fields.
     * @return Model and view.
     */
    @RequestMapping(value = "/newProject", method = {RequestMethod.POST})
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.error("Project creation failed. There are {} errors in form\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            return createProject(projectFields);
        }
        byte[] imageBytes = new byte[0];
        try {
            if (!projectFields.getImage().isEmpty())
                imageBytes = projectFields.getImage().getBytes();
        } catch (IOException e) {
            LOGGER.error("Error {} when getting bytes from MultipartFile", e.getMessage());
            return createProject(projectFields);
        }
        Project project = projectService.create(projectFields.getTitle(), projectFields.getSummary(),
                projectFields.getCost(), imageBytes, sessionUser.getId(), projectFields.getCategories());
        return new ModelAndView("redirect:/dashboard#dashboard-project-" + project.getId());
    }


}
