package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    protected SessionUserFacade sessionUser;

    /**
     * Single user profile.
     * @param id The unique user id.
     * @param back For back navigation. False if my profile.
     * @return Model and view.
     */
    @RequestMapping(value = "/users/{u_id}")
    public ModelAndView userProfile(@PathVariable("u_id") long id,
                                    @RequestParam(name = "back", defaultValue = "false") boolean back){

        final ModelAndView mav= new ModelAndView("user/profile");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
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
        final ModelAndView mav = new ModelAndView("redirect:/users/" + sessionUser.getId());
        mav.addObject("back", back);
        return mav;
    }

    /**
     * Messages view page. Investor.
     * @return Model and view
     */
    @RequestMapping(value = "/messages")
    public ModelAndView myMessages() {
        ModelAndView mav = new ModelAndView("project/myProjects");
        List<Project> projects = projectService.findByOwner(sessionUser.getId());
        mav.addObject("projects", projects);
        return mav;
    }

    @RequestMapping(value = "/messages/{id}")
    public ModelAndView singleProjectView(@PathVariable("id") long id) {
        final Project project = projectService.findById(id).orElseThrow(ProjectNotFoundException::new);
        // Prevent entrepreneurs from accessing other projects that are not theirs
        if (project.getOwner().getId() != sessionUser.getId())
            return new ModelAndView("redirect:/messages");
        final ModelAndView mav = new ModelAndView("project/singleProjectView");
        mav.addObject("project", project);
        mav.addObject("isFav", false);
        mav.addObject("contactStatus", 0);
        return mav;
    }
}
