package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.comparators.AlphComparator;
import ar.edu.itba.paw.model.comparators.CostComparator;
import ar.edu.itba.paw.model.comparators.DateComparator;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.ProjectFilter;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HelloWorldController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    private final int PAGE_SIZE = 12;



    @ModelAttribute("sessionUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // auth.getPrincipal();
        // auth.getCredentials();
        // auth.getAuthorities();
        // auth.getDetails();
        LOGGER.debug("\n\n loggedUser() called\n\n");
        if(auth != null)
            return userService.findByUsername(auth.getName()).orElse(null);
        return null;
    }

    // TODO: QUE HACEMOS ACA??
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView failedEmail() { return new ModelAndView("error"); }

    @RequestMapping("/")
    public ModelAndView index(){
        // TODO: CHANGE EXCEPTION TO SERVER ERROR?
        if (this.loggedUser().getRole() == User.UserRole.ENTREPRENEUR.getId())
            return new ModelAndView("redirect:/messages");
        // Investor logged in
        return new ModelAndView("redirect:/projects");
    }

    @RequestMapping(value = "/projects")
    public ModelAndView mainView(@ModelAttribute("categoryForm") @Valid ProjectFilter catFilter, final BindingResult errors,
                                 @RequestParam(name = "page", defaultValue ="1") String page) {
        final ModelAndView mav = new ModelAndView("mainView");
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

        Integer projects = countProjects(catFilter, catList);
        Boolean hasNext = projects > ((intPage)* PAGE_SIZE);
        List<Project> projectList = filterOrder(catFilter,catList, intPage, projects);

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



    @RequestMapping(value = "/header")
    public ModelAndView headerComponent() {
        final ModelAndView mav = new ModelAndView("header");
        return mav;
    }


    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.POST})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, final BindingResult errors, @PathVariable("id") long id,
                                          @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) throws MessagingException {
        if (errors.hasErrors()) {
            return singleProjectView(mailFields, id, false);
        }
        emailService.sendNewEmail(mailFields.getFrom(), mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(), mailFields.getTo());
        return new ModelAndView("redirect:/projects/{id}?mailSent=yes");
    }

    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.GET})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("id") long id,
                                          @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) {

        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("mailSent", mailSent);
        boolean isFav = projectService.isFavorite(id, loggedUser().getId());
        mav.addObject("isFav", isFav);
        mav.addObject("favCount", projectService.getFavoritesCount(id));

        return mav;
    }


   @RequestMapping(value = "/addFavorite", method = RequestMethod.PUT)
   @ResponseBody
    public ResponseEntity<Boolean> addFavorite(@RequestParam("p_id") int p_id, @RequestParam("u_id") int u_id) {
        projectService.addFavorite(p_id, u_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteFavorite", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> deleteFavorite(@RequestParam("p_id") int p_id, @RequestParam("u_id") int u_id) {
        projectService.deleteFavorite(p_id, u_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/addHit/{p_id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> addHit(@PathVariable("p_id") long id) {
        projectService.addHit(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @RequestMapping(value = "/newProject", method = {RequestMethod.GET})
    public ModelAndView createProject(@ModelAttribute("newProjectForm") final NewProjectFields newProjectFields) {
        final ModelAndView mav = new ModelAndView("newProject");
        List<Category> catList = categoriesService.findAll();
        catList.sort(Comparator.comparing(Category::getName));
        mav.addObject("categories", catList);
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        return mav;
    }

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

        long projectId = projectService.create(title, summary,
                projectFields.getCost(), loggedUser().getId(), projectFields.getCategories(), null, imageBytes);
        return new ModelAndView("redirect:/users/" + loggedUser().getId() + "/" + projectId);
    }




    @RequestMapping(value = "/admin")
    public ModelAndView admin(){
        final ModelAndView mav = new ModelAndView("admin");
        return mav;
    }

    @RequestMapping(value = "/users/{u_id}")
    public ModelAndView userProfile(@PathVariable("u_id") long id, @RequestParam(name = "back", defaultValue = "false") boolean back){
        final ModelAndView mav= new ModelAndView("userProfile");
        User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("list", projectService.findByOwner(id));
        List<Project> favs_projects = new ArrayList<>();
        for (Long fid : projectService.findFavorites(id)){
            favs_projects.add(projectService.findById(fid).orElseThrow(ProjectNotFoundException::new));
        }
        mav.addObject("favs", favs_projects);
        mav.addObject("back", back);
        return mav;
    }


    @RequestMapping(value = "/myProfile")
    public ModelAndView myProfile(@RequestParam(name = "back", defaultValue = "false") boolean back){
        final ModelAndView mav = new ModelAndView("redirect:/users/" + loggedUser().getId());
        mav.addObject("back", back);
        return mav;
    }

    @RequestMapping(value = "/search")
    public ModelAndView searchAux(@RequestParam("searching") String search,@RequestParam("selection") String selection, @RequestParam(name="page", defaultValue = "1") String page){
        final ModelAndView mav = new ModelAndView("search");
        int mypage = Integer.parseInt(page);
        int from = (mypage == 1) ? 0 : ((mypage -1) * PAGE_SIZE);
        Integer projects = projectService.countByCoincidence(search, selection);
        Boolean hasNext = (projects > ((mypage)* PAGE_SIZE) ) ? true : false;


        String aux = StringEscapeUtils.escapeHtml4(search.toLowerCase());

        mav.addObject("searchVal", search);
        mav.addObject("selectionVal", selection);
        mav.addObject("projectsList", projectService.findByCoincidencePage(aux,selection, from, PAGE_SIZE));
        mav.addObject("page", page);
        mav.addObject("hasNext", hasNext);
        mav.addObject("string", aux);
        return mav;
    }

    // TODO: Terminar y descomentar
    /*@RequestMapping(value = "/myProjects")
    public ModelAndView myProjects(){
        final ModelAndView mav = new ModelAndView("myProjects");
        mav.addObject("projects", projectService.findByOwner(loggedUser().getId()));
        return mav;
    }*/

    @RequestMapping(value = "/messages")
    public ModelAndView myMessages() {
        ModelAndView mav = new ModelAndView("myProjects");


        mav.addObject("projects", projectService.findByOwner(loggedUser().getId()));

        return mav;
    }

    @RequestMapping(value = "/message/{project_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> cityList(@PathVariable("project_id") long project_id) {
        return messageService.getProjectUnread(loggedUser().getId(), project_id);
    }

    @RequestMapping(value = "/message/accept/{project_id}/{sender_id}")
    public ModelAndView accept(@PathVariable("project_id") long p_id,@PathVariable("sender_id") long s_id ){
        messageService.updateMessageStatus(s_id, loggedUser().getId(), p_id, true);
        return new ModelAndView("redirect:/messages");
    }
    @RequestMapping(value = "/message/refuse/{project_id}/{sender_id}")
    public ModelAndView refuse(@PathVariable("project_id") long p_id,@PathVariable("sender_id") long s_id ){
        messageService.updateMessageStatus(s_id, loggedUser().getId(), p_id, false);
        return new ModelAndView("redirect:/messages");
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

        switch (projectFilter.getOrderBy()) {
            case "date": return auxList.stream().sorted(new DateComparator()).collect(Collectors.toList());
            case "cost-low-high": return auxList.stream().sorted(new CostComparator()).collect(Collectors.toList());
            case "cost-high-low": return auxList.stream().sorted(new CostComparator().reversed()).collect(Collectors.toList());
            case "alf": return auxList.stream().sorted(new AlphComparator()).collect(Collectors.toList());
            default: return auxList;
        }
    }

}
