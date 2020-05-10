package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

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
     * Single user profile.
     * @param id The unique user id.
     * @param back For back navigation. False if my profile.
     * @return Model and view.
     */
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

    /**
     * My profile view. Redirected to users/.
     * @param back To hide/show back button.
     * @return Model and view.
     */
    @RequestMapping(value = "/myProfile")
    public ModelAndView myProfile(@RequestParam(name = "back", defaultValue = "false") boolean back){
        final ModelAndView mav = new ModelAndView("redirect:/users/" + loggedUser().getId());
        mav.addObject("back", back);
        return mav;
    }

    /**
     * My projects. Dashboard and user own project data. Entrepreneur.
     * @return Model and view.
     */
    // TODO: Terminar y descomentar
//    @RequestMapping(value = "/myProjects")
//    public ModelAndView myProjects(){
//        final ModelAndView mav = new ModelAndView("myProjects");
//        mav.addObject("projects", projectService.findByOwner(loggedUser().getId()));
//        return mav;
//    }

    /**
     * Messages view page. Investor.
     * @return Model and view
     */
    @RequestMapping(value = "/messages")
    public ModelAndView myMessages() {
        ModelAndView mav = new ModelAndView("myProjects");
        mav.addObject("projects", projectService.findByOwner(loggedUser().getId()));
        return mav;
    }
}
