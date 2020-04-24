package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.CategoriesService;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.comparators.AlphComparator;
import ar.edu.itba.paw.model.comparators.CostComparator;
import ar.edu.itba.paw.model.comparators.DateComparator;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import ar.edu.itba.paw.webapp.mail.MailFields;
import ar.edu.itba.paw.webapp.forms.CategoryFilter;
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
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HelloWorldController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CategoriesService categoriesService;


    private User sessionUser;


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("404");
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchProject() {
        return new ModelAndView("404");
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
        public ModelAndView failedEmail() { return errorPage(0); }

    @RequestMapping("/error/{err_type}")
    public ModelAndView errorPage(@PathVariable("err_type") int err_type){
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", err_type);
        return mav;
    }
    @RequestMapping("/error")
    public ModelAndView error404(){
        final ModelAndView mav = new ModelAndView("error");
        return mav;
    }

    @RequestMapping(value = "/projects/{p_id}/contact", method = {RequestMethod.GET})
    public ModelAndView contact(@ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("p_id") int p_id) {
        final ModelAndView mav = new ModelAndView("contact");
        mav.addObject("owner", projectService.findById(p_id).orElseThrow(ProjectNotFoundException::new).getOwner());
        return mav;
    }

    @RequestMapping(value = "/projects/{p_id}/contact", method = {RequestMethod.POST})
    public ModelAndView contact(@Valid @ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("p_id") int p_id, BindingResult errors) throws MessagingException {
            if (errors.hasErrors()) {
                return contact(mailFields, p_id);
            }
           emailService.sendNewEmail(mailFields.getFrom(), mailFields.getBody(), mailFields.getTo());
           return new ModelAndView("redirect:/projects/{p_id}?mailSent=yes");
    }

    @RequestMapping("/")
    public ModelAndView index(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user= userService.findByUsername(authentication.getName());
        if(user.isPresent()) this.sessionUser = user.get();
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
    public ModelAndView mainView(@ModelAttribute("categoryForm")CategoryFilter catFilter) {
        final ModelAndView mav = new ModelAndView("mainView");

        List<Category> catList = categoriesService.findAllCats();
        List<Project> projectList = filterOrder(catFilter,catList);

        mav.addObject("cats", catList);
        mav.addObject("list", projectList);



        return mav;
    }

    @RequestMapping(value = "/header")
    public ModelAndView headerComponent() {
        final ModelAndView mav = new ModelAndView("header");
        return mav;
    }

    private List<Project> filterOrder(CategoryFilter catFilter, List<Category> catList){
        List<Project> auxList = new ArrayList<>();
        if (catFilter.getCategorySelector() != null && !catFilter.getCategorySelector().matches("allCats")) {
            Optional<Category> selectedCategory = catList.stream()
                    .filter(category -> category.getName().equals(catFilter.getCategorySelector()))
                    .findFirst();
            if (selectedCategory.isPresent()) {
                auxList = projectService.findByCategories(Collections.singletonList(selectedCategory.get()));
            }
        } else {
            auxList = projectService.findAll();
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




    // TODO> COMO LE PASO EL PROJECT CLICKEADO POR PARAMS A ESTE? ASI TENGO QUE IR DE NUEVO A LA BD
    // TODO: HACE FALTA EL REQUIRED = FALSE?
    @RequestMapping(value = "/projects/{id}")
    public ModelAndView singleProjectView(@PathVariable("id") long id,
                                          @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("mailSent", mailSent);
        return mav;
    }

    /*@RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView register(@RequestParam(name = "username", required = true) String username) {
        final User user = userService.create(username);
        return new ModelAndView("redirect:/" + user.getId());
    }*/

    @RequestMapping(value = "/newProject", method = {RequestMethod.GET})
    public ModelAndView createProject(@ModelAttribute("newProjectForm") final NewProjectFields newProjectFields) {
        final ModelAndView mav = new ModelAndView("newProject");
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("categories", catList);
        return mav;
    }

    @RequestMapping(value = "/newProject", method = {RequestMethod.POST})
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields, BindingResult errors){
        if (errors.hasErrors()) {
            return createProject(projectFields);
        }
        // TODO: GUARDAR UNA SESION PARA TENER EL OWNER ID, POR AHORA HARDCODEO 1
        // TODO: AGREGAR STAGES
        long projectId = projectService.create(projectFields.getTitle(), projectFields.getSummary(),
                projectFields.getCost(), 1, projectFields.getCategories(), null);
        return new ModelAndView("redirect:/projects/" + projectId);
    }


    @RequestMapping(value = "/login")
    public ModelAndView login(){
        final ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @RequestMapping(value = "/admin")
    public ModelAndView admin(){
        final ModelAndView mav = new ModelAndView("admin");
        return mav;
    }


    @RequestMapping(value = "/signUp")
    public ModelAndView signUp( @ModelAttribute("userForm") final NewUserFields userFields){
        final ModelAndView mav = new ModelAndView("signUp");
        return mav;
    }

    @RequestMapping(value = "/signUp", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields, final BindingResult errors){
        if(errors.hasErrors()){
            return signUp(userFields);
        }

        //TODO transaction here 
        User user = userService.create( userFields.getFirstName(), userFields.getLastName(), userFields.getRealId(),new Date(userFields.getYear(),userFields.getMonth(),userFields.getDay()), new Location(new Location.Country(1,"","","",""), new Location.State(1, "", ""), new Location.City(1, "")), userFields.getEmail(),userFields.getPhone(),userFields.getLinkedin(),"HOLA",new Date(),0);
        userService.createPassword(user.getId(), userFields.getPassword());
        final ModelAndView mav = new ModelAndView("redirect:/login");
        return mav;
    }


    @RequestMapping(value = "/users/{u_id}")
    public ModelAndView userProfile(@PathVariable("u_id") long id){
        final ModelAndView mav= new ModelAndView("userProfile");
        User user = userService.findById(id).orElseThrow(NoClassDefFoundError::new);
        mav.addObject("user", user);
        mav.addObject("list", projectService.findByOwner(id));
        System.out.println(userService.findById(id));
        return mav;
    }


    @RequestMapping(value = "/myProfile")
    public ModelAndView myProfile(){
        final ModelAndView mav = new ModelAndView("redirect:/users/" + sessionUser.getId());
        return mav;
    }
}
