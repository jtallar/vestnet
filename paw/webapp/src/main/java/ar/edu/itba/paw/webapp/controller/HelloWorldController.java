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
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import ar.edu.itba.paw.webapp.mail.MailFields;
import ar.edu.itba.paw.webapp.forms.CategoryFilter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
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
        return userService.findByUsername(auth.getName()).orElse(null);
    }

    // TODO: QUE HACEMOS ACA??
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView failedEmail() { return new ModelAndView("error"); }

    /*@ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ModelAndView failedRegistration() { return new ModelAndView("error"); }*/

    @RequestMapping(value = "/projects/{p_id}/contact", method = {RequestMethod.GET})
    public ModelAndView contact(@ModelAttribute("mailForm") final MailFields mailFields, @PathVariable("p_id") int p_id) {
        final ModelAndView mav = new ModelAndView("contact");
        mav.addObject("owner", projectService.findById(p_id).orElseThrow(ProjectNotFoundException::new).getOwner());
        mav.addObject("p_id", p_id);
        // TODO: SACAR SI PERSISTIMOS CON @MODEL ATTRIBUTE
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
        mav.addObject("back", "/projects");
        mav.addObject("investor", true);
       // mav.addObject("isFav", true);
        boolean isFav = projectService.isFavorite(id, loggedUser().getId());
        mav.addObject("isFav", isFav);

        return mav;
    }


   @RequestMapping(value = "/projects/{p_id}/addFavorite", method = RequestMethod.PUT)
   @ResponseBody
    public ResponseEntity<Boolean> addFavorite(@PathVariable("p_id") int p_id, @RequestParam("u_id") int u_id) {
        projectService.addFavorite(p_id, u_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{p_id}//deleteFavorite", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Boolean> deleteFavorite(@PathVariable("p_id") int p_id, @RequestParam("u_id") int u_id) {
        projectService.deleteFavorite(p_id, u_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @RequestMapping(value = "/newProject", method = {RequestMethod.GET})
    public ModelAndView createProject(@ModelAttribute("newProjectForm") final NewProjectFields newProjectFields) {
        final ModelAndView mav = new ModelAndView("newProject");
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("categories", catList);
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        return mav;
    }

    @RequestMapping(value = "/newProject", method = {RequestMethod.POST})
    public ModelAndView createProject(@Valid @ModelAttribute("newProjectForm") final NewProjectFields projectFields, BindingResult errors){
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
        // TODO: AGREGAR STAGES
        long projectId = projectService.create(projectFields.getTitle(), projectFields.getSummary(),
                projectFields.getCost(), loggedUser().getId(), projectFields.getCategories(), null, imageBytes);
        return new ModelAndView("redirect:/users/" + loggedUser().getId() + "/" + projectId);
    }

    @RequestMapping(value = "/imageController/project/{p_id}",
            produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] imageControllerProject(@PathVariable("p_id") long id) {
        // Si no tiene pic --> Devuelve null
        // TODO: CHANGE NO IMAGE PIC
        byte[] image = projectService.findImageForProject(id);
        if (image == null) {
            try {
                Resource stockImage = new ClassPathResource("projectNoImage.png");
                image = IOUtils.toByteArray(stockImage.getInputStream());
            } catch (IOException e) {
                LOGGER.debug("Could not load stock image");
            }
        }
        return image;
    }

    @RequestMapping(value = "/imageController/user/{u_id}")
    @ResponseBody
    public byte[] imageControllerUser(@PathVariable("u_id") long id) {
        // Si no tiene pic --> Devuelve null
        // TODO: CHANGE NO IMAGE PIC
        byte[] image = userService.findImageForUser(id);
        if (image == null) {
            try {
                Resource stockImage = new ClassPathResource("userNoImage.png");
                image = IOUtils.toByteArray(stockImage.getInputStream());
            } catch (IOException e) {
                LOGGER.debug("Could not load stock image. Error {}", e.getMessage());
            }
        }
        return image;
    }




    @RequestMapping(value = "/admin")
    public ModelAndView admin(){
        final ModelAndView mav = new ModelAndView("admin");
        return mav;
    }

    @RequestMapping(value = "/users/{u_id}")
    public ModelAndView userProfile(@PathVariable("u_id") long id){
        final ModelAndView mav= new ModelAndView("userProfile");
        User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("list", projectService.findByOwner(id));
        List<Project> favs_projects = new ArrayList<>();
        for (Long fid : projectService.findFavorites(id)){
            favs_projects.add(projectService.findById(fid).orElseThrow(ProjectNotFoundException::new));
        }
        mav.addObject("favs", favs_projects);
        return mav;
    }

    @RequestMapping(value = "/users/{u_id}/{p_id}")
    public ModelAndView userProjectView(@PathVariable("u_id") long userId, @PathVariable("p_id") long projectId) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(projectId).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("back", "/users/" + userId);
        mav.addObject("investor", loggedUser().getRole() == User.UserRole.INVESTOR.getId());
        boolean isFav = projectService.isFavorite(projectId, userId);
        mav.addObject("isFav", isFav);
//        mav.addObject("mailSent", mailSent);
        return mav;
    }

    @RequestMapping(value = "/myProfile")
    public ModelAndView myProfile(){
        final ModelAndView mav = new ModelAndView("redirect:/users/" + loggedUser().getId());
        return mav;
    }

    @RequestMapping(value = "/search")
    public ModelAndView searchAux(@RequestParam("searching") String search){
        final ModelAndView mav = new ModelAndView("search");
        String aux = StringEscapeUtils.escapeHtml4(search.toLowerCase());
        if(loggedUser().getRole() == 2) {
            mav.addObject("projectsList", projectService.findCoincidence(aux));
        } //only want to show users projects if is an investor
        mav.addObject("usersList", userService.findCoincidence(aux));
        mav.addObject("string", aux);
        System.out.println(aux);
        return mav;
    }

    @RequestMapping(value = "/headerFirstOption")
    public ModelAndView getHeaderFirstOption() {
        if (loggedUser().getRole() == 1) {
            // Entrepreneur
            return new ModelAndView("redirect:/newProject");
        }
        // Investor
        return new ModelAndView("redirect:/projects");
    }

    // TODO: Terminar y descomentar
    /*@RequestMapping(value = "/myProjects")
    public ModelAndView myProjects(){
        final ModelAndView mav = new ModelAndView("myProjects");
        mav.addObject("projects", projectService.findByOwner(loggedUser().getId()));
        return mav;
    }*/

}
