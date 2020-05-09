package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.CategoriesService;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.comparators.AlphComparator;
import ar.edu.itba.paw.model.comparators.CostComparator;
import ar.edu.itba.paw.model.comparators.DateComparator;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.MailFields;
import ar.edu.itba.paw.webapp.forms.CategoryFilter;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private CategoriesService categoriesService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    private final int PAGE_SIZE = 12;



    /*@ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error");
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchProject() {
        return new ModelAndView("error");
    }*/

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

    /*@ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ModelAndView failedRegistration() { return new ModelAndView("error"); }*/

    @RequestMapping("/")
    public ModelAndView index(){
        // TODO: CHANGE EXCEPTION TO SERVER ERROR?
        if (this.loggedUser().getRole() == User.UserRole.ENTREPRENEUR.getId())
            return new ModelAndView("redirect:/myProfile");
        // Investor logged in
        return new ModelAndView("redirect:/projects");
    }

    /*@RequestMapping("/{id}")
    public ModelAndView helloWorld(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("cats", catList);
        return mav;
    }*/

    @RequestMapping(value = "/projects")
    public ModelAndView mainView( @ModelAttribute("categoryForm") @Valid CategoryFilter catFilter, final BindingResult errors,
                                  @RequestParam(name = "page", defaultValue ="1") String page) {
        final ModelAndView mav = new ModelAndView("mainView");
        Integer intPage = Integer.parseInt(page);
        List<Category> catList = categoriesService.findAllCats();


        if(errors.hasErrors()){
            Integer projects = 0;
            Boolean hasNext = false;
            page = "1";
            List<Project> projectList = new ArrayList<>();
            mav.addObject("hasNext",hasNext);
            mav.addObject("page", page);
            mav.addObject("cats", catList);
            mav.addObject("list", projectList);

            return mav;
        }

        /*
        if(max != null){
            catFilter.setMax(max);
        }
        if (min != null){
            catFilter.setMin(min);
        }
        if(catSel != null){
            catFilter.setCategorySelector(catSel);
        }
        if (orderBy != null){
            catFilter.setOrderBy(orderBy);
        }

         */




        Integer projects = countProjects(catFilter, catList);
        List<Project> projectList = filterOrder(catFilter,catList, intPage, projects);

        Boolean hasNext = (projects > ((intPage)* PAGE_SIZE) ) ? true : false;
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

    private Integer countProjects(CategoryFilter catFilter, List<Category> catList){
        long minAux, maxAux;
        minAux = (catFilter.getMin() == null || catFilter.getMin().matches("")) ? 0 : Long.parseLong(catFilter.getMin());
        maxAux = (catFilter.getMax() == null || catFilter.getMax().matches("")) ? Long.MAX_VALUE : Long.parseLong(catFilter.getMax());

        Integer projects;
        if(catFilter.getCategorySelector() == null || catFilter.getCategorySelector().matches("allCats")){ //calculate total projects to render to check limit and offset
            projects = projectService.projectsCount(minAux, maxAux);
        }
        else {
            Optional<Category> selectedCategory = catList.stream()
                    .filter(category -> category.getName().equals(catFilter.getCategorySelector()))
                    .findFirst();
            projects = projectService.catProjCount(Collections.singletonList(selectedCategory.get()), minAux, maxAux);
        }
        return projects;
    }


    private List<Project> filterOrder(CategoryFilter catFilter, List<Category> catList, Integer page, Integer projects){
        long minAux, maxAux;
        minAux = (catFilter.getMin() == null || catFilter.getMin().matches("")) ? 0 : Long.parseLong(catFilter.getMin());
        maxAux = (catFilter.getMax() == null || catFilter.getMax().matches("")) ? Long.MAX_VALUE : Long.parseLong(catFilter.getMax());




        int from = (page == 1) ? 0 : ((page -1) * PAGE_SIZE);
        int size = ((projects - from) < PAGE_SIZE) ? (projects - from) : PAGE_SIZE;


        List<Project> auxList = new ArrayList<>();
        if (catFilter.getCategorySelector() != null && !catFilter.getCategorySelector().matches("allCats")) {
            Optional<Category> selectedCategory = catList.stream()
                    .filter(category -> category.getName().equals(catFilter.getCategorySelector()))
                    .findFirst();
            if (selectedCategory.isPresent()) {

                auxList = projectService.findCatForPage(Collections.singletonList(selectedCategory.get()), from, size, minAux, maxAux);

            }
        } else {

            auxList = projectService.findPage(from, size, minAux, maxAux);
        }

        if(catFilter.getOrderBy() != null) {
            switch (catFilter.getOrderBy()) {
                case "date":
                    auxList = auxList.stream().sorted(new DateComparator()).collect(Collectors.toList());
                    break;
                case "cost-low-high":
                    auxList = auxList.stream().sorted(new CostComparator()).collect(Collectors.toList());
                    break;
                case "cost-high-low":
                    auxList = auxList.stream().sorted(new CostComparator().reversed()).collect(Collectors.toList());
                    break;
                case "alf":
                    auxList = auxList.stream().sorted(new AlphComparator()).collect(Collectors.toList());
                    break;
            }
        }

        return auxList;
    }


    @RequestMapping(value = "/header")
    public ModelAndView headerComponent() {
        final ModelAndView mav = new ModelAndView("header");
        return mav;
    }



//    @RequestMapping(value = "/projects/{p_id}", method = {RequestMethod.GET})
//    public ModelAndView contact(@ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("p_id") int p_id) {
//        final ModelAndView mav = new ModelAndView("contact");
//        mav.addObject("owner", projectService.findById(p_id).orElseThrow(ProjectNotFoundException::new).getOwner());
//        mav.addObject("p_id", p_id);
//        // TODO: SACAR SI PERSISTIMOS CON @MODEL ATTRIBUTE
//        return mav;
//    }
//
//    @RequestMapping(value = "/projects/{p_id}", method = {RequestMethod.POST})
//    public ModelAndView contact(@Valid @ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("p_id") int p_id, BindingResult errors) throws MessagingException {
//        if (errors.hasErrors()) {
//            return contact(mailFields, p_id);
//        }
//        emailService.sendNewEmail(mailFields.getFrom(), mailFields.getBody(), mailFields.getTo());
//        return new ModelAndView("redirect:/projects/{p_id}?mailSent=yes");
//    }

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
        List<Category> catList = categoriesService.findAllCats();
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

        // TODO: AGREGAR STAGES
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

    /*@RequestMapping(value = "/users/{u_id}/{p_id}")
    public ModelAndView userProjectView(@ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("u_id") long userId, @PathVariable("p_id") long projectId,
                                        @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(projectId).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("mailSent", mailSent);
        mav.addObject("back", "/users/" + userId);
        boolean isFav = projectService.isFavorite(projectId, loggedUser().getId());
        mav.addObject("isFav", isFav);
        mav.addObject("favCount", projectService.getFavoritesCount(projectId));

        return mav;
    }

    @RequestMapping(value = "/users/{u_id}/{p_id}", method = {RequestMethod.POST})
    public ModelAndView userProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields, final BindingResult errors,
                                        @PathVariable("u_id") long userId, @PathVariable("p_id") long projectId,
                                        @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) throws MessagingException {
        if (errors.hasErrors()) {
            return userProjectView(mailFields, userId, projectId, false);
        }
        emailService.sendNewEmail(mailFields.getFrom(), mailFields.getBody(), mailFields.getOffers(), mailFields.getExchange(), mailFields.getTo());
        return new ModelAndView("redirect:/users/{u_id}/{p_id}?mailSent=yes");
    }*/

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
        Integer projects = projectService.searchProjCount(search, selection);
        Boolean hasNext = (projects > ((mypage)* PAGE_SIZE) ) ? true : false;


        String aux = StringEscapeUtils.escapeHtml4(search.toLowerCase());

        mav.addObject("searchVal", search);
        mav.addObject("selectionVal", selection);
        mav.addObject("projectsList", projectService.findCoincidence(aux,selection, from, PAGE_SIZE));
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
        return new ModelAndView("redirect:/projects");
    }

}
