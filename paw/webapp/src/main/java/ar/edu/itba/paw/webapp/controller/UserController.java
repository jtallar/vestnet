package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

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
                                    @RequestParam(name = "back", defaultValue = "false") boolean back) {

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
    @RequestMapping(value = "/profile")
    public ModelAndView myProfile(@RequestParam(name = "back", defaultValue = "false") boolean back) {
        return userProfile(sessionUser.getId(), back);
    }


    /**
     * Messages view page. Entrepreneur.
     * @return Model and view
     */
    @RequestMapping(value = "/dashboard")
    public ModelAndView myDashboard(@RequestParam(name = "funded", defaultValue = "false") boolean funded) {

        ModelAndView mav = new ModelAndView("user/dashboard");
        mav.addObject("projects", userService.getOwnedProjects(sessionUser.getId(), funded));
        mav.addObject("funded", funded);
        return mav;
    }


    /** This are constant for now */
    private static final Integer PAGE_SIZE = 6;

    /**
     * Deals vew page. Entrepreneur.
     * @return Model and view.
     */
    @RequestMapping(value = "/deals")
    public ModelAndView myDeals(@RequestParam(name = "page", defaultValue = "1") Integer page) {

        final ModelAndView mav = new ModelAndView("user/deals");
        mav.addObject("messagePage", userService.getAcceptedMessages(sessionUser.getId(), page, PAGE_SIZE));
        return mav;
    }


    /**
     * Requests made view page. Investor.
     * @return Model and view.
     */
    @RequestMapping("/requests")
    public ModelAndView myRequests(@RequestParam(name = "page", defaultValue = "1") Integer page) {

        final ModelAndView mav = new ModelAndView("user/requests");
        mav.addObject("messagePage", userService.getOfferMessages(sessionUser.getId(), page, PAGE_SIZE));
        return mav;
    }

}
