package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.Pair;
import ar.edu.itba.paw.model.components.ProjectFilter;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.ProjectFilterForm;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private static final int PAGE_SIZE = 2;
    private static final Integer FIRST_PAGE = 1;
    private static final int PAGINATION_ITEMS = 5;

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

    /**
     * Session user data.
     *
     * @return The logged in user.
     */
    @ModelAttribute("sessionUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("\n\n loggedUser() called\n\n");
        if (auth != null)
            return userService.findByUsername(auth.getName()).orElse(null);
        return null;
    }

    /**
     */
    @RequestMapping(value = "/projects", method = {RequestMethod.GET})
    public ModelAndView mainView(@ModelAttribute("categoryForm") @Valid ProjectFilterForm form,
                                 final BindingResult error,
                                 @RequestParam(name = "keyword", required = false) String keyword,
                                 @RequestParam(name = "searchField", required = false) String searchField,
                                 @RequestParam(name = "page", defaultValue = "1") Integer page) {

        page = (page < 0) ? 1 : page;
        ProjectFilter projectFilter = new ProjectFilter(page, PAGE_SIZE);
        projectFilter.setSearch(StringEscapeUtils.escapeHtml4(keyword), searchField);
        projectFilter.setCost(form.getMinCost(), form.getMaxCost());
        projectFilter.setCategory(form.getCategoryId());
        projectFilter.setSort(form.getOrderBy());

        List<Project> projects = projectService.findFiltered(projectFilter);
        Integer projectCount = projectService.countFiltered(projectFilter);
        if (projects.size() == 0) return new ModelAndView("redirect:/projects?page=1");
        Pair<Integer, Integer> paginationLimits = setPaginationLimits(projectCount, page);

        final ModelAndView mav = new ModelAndView("project/viewProjectFeed");
        mav.addObject("categories", categoriesService.findAll());
        mav.addObject("projects", projects);
        mav.addObject("keyword", StringEscapeUtils.escapeHtml4(keyword));
        mav.addObject("searchField", searchField);
        mav.addObject("startPage", paginationLimits.getKey());
        mav.addObject("endPage", paginationLimits.getValue());
        mav.addObject("page", page);

        User loggedUser = loggedUser();
        if (loggedUser != null && loggedUser.getRole() == User.UserRole.INVESTOR.getId())
            mav.addObject("isFav", projectService.isFavorite(projects.stream().map(Project::getId).collect(Collectors.toList()), loggedUser.getId()));
        else
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
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("id") long id,
                                          @RequestParam(name = "contactStatus", defaultValue = "0") int contactStatus) {

        final ModelAndView mav = new ModelAndView("project/singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("contactStatus", contactStatus);
        boolean isFav = projectService.isFavorite(id, loggedUser().getId());
        mav.addObject("isFav", isFav);
        mav.addObject("favCount", projectService.getFavoritesCount(id));

        return mav;
    }

    /**
     * Message not set exception handler
     *
     * @return Model and view.
     */
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView failedEmail() {
        return new ModelAndView("error/error");
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
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, final BindingResult errors, @PathVariable("id") long id, HttpServletRequest request) throws MessagingException {
        if (errors.hasErrors()) {
            return singleProjectView(mailFields, id, 0);
        }
        User loggedUser = loggedUser();

        try {
            messageService.create(mailFields.getBody(), String.valueOf(mailFields.getOffers()), mailFields.getExchange(), loggedUser.getId(), mailFields.getToId(), id);
        } catch (MessageAlreadySentException e) {
            return singleProjectView(mailFields, id, 2);
        }

        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        LOGGER.debug("\n\nLocale: {}\n\n", mailFields.getLocale());
        emailService.sendNewEmail(loggedUser, mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(),
                mailFields.getTo(), mailFields.getProject(), id, baseUrl, mailFields.getLocale());
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
        List<Category> catList = categoriesService.findAll();
        catList.sort(Comparator.comparing(Category::getName));
        mav.addObject("categories", catList);
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
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createProject(projectFields);
        }
        byte[] imageBytes = new byte[0];
        try {
            if (!projectFields.getImage().isEmpty())
                imageBytes = projectFields.getImage().getBytes();
        } catch (IOException e) {
            return createProject(projectFields);
        }

        String title = StringEscapeUtils.escapeHtml4(projectFields.getTitle());
        String summary = StringEscapeUtils.escapeHtml4(projectFields.getSummary());
        long userId = loggedUser().getId();

        long projectId = projectService.create(title, summary,
                projectFields.getCost(), userId, projectFields.getCategories(), null, imageBytes);
        return new ModelAndView("redirect:/messages#dashboard-project-" + projectId);
    }

    /**
     * Creates the pagination logic.
     * @param projectCount The count of projects to paginate.
     * @param page The current pagination page.
     * @return A pair set as <startPage, endPage>
     */
    private Pair<Integer, Integer> setPaginationLimits(Integer projectCount, Integer page) {
        int maxPages = (projectCount + 1) / PAGE_SIZE;
        if (maxPages <= PAGINATION_ITEMS) return new Pair<>(FIRST_PAGE, maxPages);
        int firstPage = page - PAGINATION_ITEMS / 2;
        if (firstPage <= FIRST_PAGE ) return new Pair<>(FIRST_PAGE, PAGINATION_ITEMS);
        int lastPage = page + PAGINATION_ITEMS / 2;
        if (lastPage <= maxPages) return new Pair<>(firstPage, lastPage);
        return new Pair<>(maxPages - PAGINATION_ITEMS, maxPages);
    }
}
