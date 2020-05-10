package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.comparators.AlphComparator;
import ar.edu.itba.paw.model.comparators.CostComparator;
import ar.edu.itba.paw.model.comparators.DateComparator;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.ProjectFilter;
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
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private final int PAGE_SIZE = 12;

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
     * Maps main view with all projects. It filters and sorts them.
     * @param projectFilter Filter for all the projects.
     * @param errors Errors.
     * @param page Current page.
     * @return Model and view.
     */
    @RequestMapping(value = "/projects")
    public ModelAndView mainView(@ModelAttribute("categoryForm") @Valid ProjectFilter projectFilter, final BindingResult errors,
                                 @RequestParam(name = "page", defaultValue ="1") String page) {
        final ModelAndView mav = new ModelAndView("project/viewProjectFeed");
        Integer intPage = Integer.parseInt(page);
        List<Category> catList = categoriesService.findAll();

        if(errors.hasErrors()){
            List<Project> projectList = new ArrayList<>();
            mav.addObject("hasNext", false);
            mav.addObject("page", "1");
            mav.addObject("cats", catList);
            mav.addObject("list", projectList);
            return mav;
        }

        Integer projects = countProjects(projectFilter, catList);
        Boolean hasNext = projects > ((intPage)* PAGE_SIZE);
        List<Project> projectList = filterOrder(projectFilter,catList, intPage, projects);

        mav.addObject("hasNext",hasNext);
        mav.addObject("page", page);
        mav.addObject("cats", catList);
        mav.addObject("list", projectList);

        User loggedUser = loggedUser();
        if (loggedUser != null && loggedUser.getRole() == User.UserRole.INVESTOR.getId())
            mav.addObject("isFav", projectService.isFavorite(projectList.stream().map(Project::getId).collect(Collectors.toList()), loggedUser.getId()));
        else
            mav.addObject("isFav", new ArrayList<>());

        return mav;
    }


    /**
     * Search a text in all projects for matches in the selected field.
     * @param search Text to search for matches
     * @param selection Selection of what to search.
     * @param page Current page.
     * @return Model and view.
     */
    @RequestMapping(value = "/search")
    public ModelAndView searchAux(@RequestParam("searching") String search,@RequestParam("selection") String selection, @RequestParam(name="page", defaultValue = "1") String page){
        final ModelAndView mav = new ModelAndView("searchProjects");
        int myPage = Integer.parseInt(page);
        int from = (myPage - 1) * PAGE_SIZE;
        Integer projects = projectService.countByCoincidence(search, selection);
        Boolean hasNext = projects > ((myPage) * PAGE_SIZE);


        String aux = StringEscapeUtils.escapeHtml4(search.toLowerCase());

        mav.addObject("searchVal", search);
        mav.addObject("selectionVal", selection);
        mav.addObject("projectsList", projectService.findByCoincidencePage(aux,selection, from, PAGE_SIZE));
        mav.addObject("page", page);
        mav.addObject("hasNext", hasNext);
        mav.addObject("string", aux);
        return mav;
    }


    /**
     * Single project view page.
     * @param mailFields Fields for mail contact
     * @param id The unique project id.
     * @param contactStatus Boolean if mail was sent.
     * @return Model and view.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.GET})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("id") long id,
                                          @RequestParam(name = "contactStatus", defaultValue = "0") int contactStatus ) {

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
     * @return Model and view.
     */
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView failedEmail() { return new ModelAndView("error/error"); }

    /**
     * Post method. Used when contacted project owner.
     * @param mailFields Fields for mail contact
     * @param errors Errors on form.
     * @param id The unique project id.
     * @return Model and view.
     * @throws MessagingException When mail cannot be sent.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.POST})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, final BindingResult errors, @PathVariable("id") long id) throws MessagingException {
        if (errors.hasErrors()) {
            return singleProjectView(mailFields, id, 0);
        }

        try {
            messageService.create(mailFields.getBody(), String.valueOf(mailFields.getOffers()), mailFields.getExchange(), loggedUser().getId(), mailFields.getToId(), id);
        } catch (MessageAlreadySentException e) {
            return singleProjectView(mailFields, id, 2);
        }
        emailService.sendNewEmail(mailFields.getFrom(), mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(), mailFields.getTo());
        return new ModelAndView("redirect:/projects/{id}?contactStatus=1");
    }

    /**
     * Shows the new project form.
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
     * @param projectFields The filled form fields for new project.
     * @param errors Errors on the form fields.
     * @return Model and view.
     */
    @RequestMapping(value = "/newProject", method = {RequestMethod.POST})
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields, final BindingResult errors){
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
     * Auxiliary functions
     */

    /**
     * Counts the quantity of projects that matches filter criteria.
     * @param projectFilter The filter to be matched.
     * @param categories All the categories.
     * @return The count of projects.
     */
    private Integer countProjects(ProjectFilter projectFilter, List<Category> categories) {
        long minCost, maxCost;
        minCost = (projectFilter.getMinCost() == null || projectFilter.getMinCost().matches("")) ? 0 : Long.parseLong(projectFilter.getMinCost());
        maxCost = (projectFilter.getMaxCost() == null || projectFilter.getMaxCost().matches("")) ? Long.MAX_VALUE : Long.parseLong(projectFilter.getMaxCost());

        if(projectFilter.getCategorySelector() == null || projectFilter.getCategorySelector().matches("allCats")) //calculate total projects to render to check limit and offset
            return projectService.countByCost(minCost, maxCost);

        Optional<Category> selectedCategory = categories.stream()
                .filter(category -> category.getName().equals(projectFilter.getCategorySelector()))
                .findFirst();
        return projectService.countByCategory(Collections.singletonList(selectedCategory.get()), minCost, maxCost);
    }

    /**
     * Gets a filtered and ordered list of projects.
     * @param projectFilter The criteria to match.
     * @param categories All the categories.
     * @param currentPage Current page of the project.
     * @param projects The quantity of projects.
     * @return List of all the project sorted, that match criteria.
     */
    private List<Project> filterOrder(ProjectFilter projectFilter, List<Category> categories, Integer currentPage, Integer projects) {
        long minCost, maxCost;
        minCost = (projectFilter.getMinCost() == null || projectFilter.getMinCost().matches("")) ? 0 : Long.parseLong(projectFilter.getMinCost());
        maxCost = (projectFilter.getMaxCost() == null || projectFilter.getMaxCost().matches("")) ? Long.MAX_VALUE : Long.parseLong(projectFilter.getMaxCost());

        int startPage = ((currentPage - 1) * PAGE_SIZE);
        int pageOffset = Math.min((projects - startPage), PAGE_SIZE);

        List<Project> auxList = new ArrayList<>();
        if (projectFilter.getCategorySelector() != null && !projectFilter.getCategorySelector().matches("allCats")) {
            Optional<Category> selectedCategory = categories.stream()
                    .filter(category -> category.getName().equals(projectFilter.getCategorySelector()))
                    .findFirst();
            if (selectedCategory.isPresent()) {
                auxList = projectService.findByCategoryPage(Collections.singletonList(selectedCategory.get()), startPage, pageOffset, minCost, maxCost);
            }
        } else {
            auxList = projectService.findByCostPage(startPage, pageOffset, minCost, maxCost);
        }

        if (projectFilter.getOrderBy() == null) return auxList;

        switch (projectFilter.getOrderBy()) {
            case "date": return auxList.stream().sorted(new DateComparator()).collect(Collectors.toList());
            case "cost-low-high": return auxList.stream().sorted(new CostComparator()).collect(Collectors.toList());
            case "cost-high-low": return auxList.stream().sorted(new CostComparator().reversed()).collect(Collectors.toList());
            case "alf": return auxList.stream().sorted(new AlphComparator()).collect(Collectors.toList());
            default: return auxList;
        }
    }
}
