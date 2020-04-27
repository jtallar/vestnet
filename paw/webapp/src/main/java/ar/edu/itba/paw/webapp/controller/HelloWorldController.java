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
import ar.edu.itba.paw.webapp.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.NewProjectFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import ar.edu.itba.paw.webapp.mail.MailFields;
import ar.edu.itba.paw.webapp.forms.CategoryFilter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
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

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    // TODO: Check if there is another way to persist user for the controller
    private User sessionUser;


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
        mav.addObject("sessionUser", sessionUser);
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
        // TODO: CHANGE EXCEPTION TO SERVER ERROR?
        this.sessionUser = userService.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        if (this.sessionUser.getRole() == User.UserRole.ENTREPRENEUR.getId())
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

    @RequestMapping(value = "/projects/{id}")
    public ModelAndView singleProjectView(@PathVariable("id") long id,
                                          @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("mailSent", mailSent);
        mav.addObject("back", "/projects");
        mav.addObject("owner", false);
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
        byte[] imageBytes = new byte[0];
        try {
            if (!projectFields.getImage().isEmpty())
                imageBytes = projectFields.getImage().getBytes();
        } catch (IOException e) {
            return createProject(projectFields);
        }
        // TODO: AGREGAR STAGES
        long projectId = projectService.create(projectFields.getTitle(), projectFields.getSummary(),
                projectFields.getCost(), sessionUser.getId(), projectFields.getCategories(), null, imageBytes);
        return new ModelAndView("redirect:/users/" + sessionUser.getId() + "/" + projectId);
    }

    @RequestMapping(value = "/imageController/project/{p_id}")
    @ResponseBody
    public byte[] imageControllerProject(@PathVariable("p_id") long id) {
        // Si no tiene pic --> Devuelve null
        // TODO: CHANGE NO IMAGE PIC
        byte[] image = projectService.findImageForProject(id);
        if (image == null) {
            try {
                Resource stockImage = new ClassPathResource("noImage.png");
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
                Resource stockImage = new ClassPathResource("noImage.png");
                image = IOUtils.toByteArray(stockImage.getInputStream());
            } catch (IOException e) {
                LOGGER.debug("Could not load stock image");
            }
        }
        return image;
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
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields, final BindingResult errors, HttpServletRequest request){
        if(errors.hasErrors()){
            LOGGER.debug("\n\nSign Up failed. There are {} errors\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.debug("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            LOGGER.debug("\n\n");
            return signUp(userFields);
        }
        byte[] imageBytes = new byte[0];
        try {
            if (!userFields.getProfilePicture().isEmpty())
                imageBytes = userFields.getProfilePicture().getBytes();
        } catch (IOException e) {
            return signUp(userFields);
        }

        //TODO add location when working
        final long userId = userService.create(userFields.getRole(), userFields.getFirstName(), userFields.getLastName(), userFields.getRealId(),
                LocalDate.of(userFields.getYear(), userFields.getMonth(), userFields.getDay()),
//                new Location(userFields.getCountry(), userFields.getState(), userFields.getCity()),
                new Location(new Location.Country(userFields.getCountry(), "", "", "", ""),
                        new Location.State(userFields.getState(), "", ""), new Location.City(userFields.getCity(), "")),
                userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), userFields.getPassword(), imageBytes);
//        userService.createPassword(user.getId(), userFields.getPassword());
        if (userId <= 0) {
            // TODO: VER COMO TIRAR UNA EXCEPCION CON UN CODIGO DE ERROR NUESTRO
//            throw new UserAlreadyExistsException();
            return signUp(userFields);
        }

        // Auto Log In
        authenticateUserAndSetSession(userFields.getEmail(), userFields.getPassword(), request);
        return new ModelAndView("redirect:/");
    }

    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    @RequestMapping(value = "/users/{u_id}")
    public ModelAndView userProfile(@PathVariable("u_id") long id){
        final ModelAndView mav= new ModelAndView("userProfile");
        User user = userService.findById(id).orElseThrow(NoClassDefFoundError::new);
        mav.addObject("user", user);
        mav.addObject("list", projectService.findByOwner(id));
        return mav;
    }

    @RequestMapping(value = "/users/{u_id}/{p_id}")
    public ModelAndView userProjectView(@PathVariable("u_id") long userId, @PathVariable("p_id") long projectId) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(projectId).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("back", "/users/" + userId);
        mav.addObject("owner", true);
//        mav.addObject("mailSent", mailSent);
        return mav;
    }

    @RequestMapping(value = "/myProfile")
    public ModelAndView myProfile(){
        final ModelAndView mav = new ModelAndView("redirect:/users/" + sessionUser.getId());
        return mav;
    }

    // TODO: CHECK IF ITS THE RIGHT WAY TO DO THIS
    @RequestMapping(value = "/location/countries")
    public List<Location.Country> countryList() {
        return Arrays.asList(new Location.Country(1, "Nombre1", "ds", "ads", "dsa"), new Location.Country(2, "Nombre2", "ds", "ads", "dsa"),
                new Location.Country(3, "Nombre3", "ds", "ads", "dsa"), new Location.Country(4, "Nombre1", "ds", "ads", "dsa"));
    }

    @RequestMapping(value = "/location/states/{country_id}")
    public List<Location.State> stateList(@PathVariable("country_id") long countryId) {
        if (countryId == 0) return null;
        return Arrays.asList(new Location.State(1, "State1", "11"), new Location.State(2, "State2", "11"), new Location.State(3, "State3", "11"));
    }

    @RequestMapping(value = "/location/cities/{state_id}")
    public List<Location.City> cityList(@PathVariable("state_id") long stateId) {
        if (stateId == 0) return null;
        return Arrays.asList(new Location.City(1, "City1"), new Location.City(2, "City2"), new Location.City(3, "City3"));
    }


    @RequestMapping(value = "/search")
    public ModelAndView searchAux(@RequestParam("searching") String search){
        final ModelAndView mav = new ModelAndView("search");
        String aux = search.toLowerCase();

        mav.addObject("projectsList", projectService.findCoincidence(aux));
        mav.addObject("usersList", userService.findCoincidence(aux));
        mav.addObject("string", search);

        return mav;
    }
}
