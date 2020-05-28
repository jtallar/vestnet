package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    protected SessionUserFacade sessionUser;


    /**
     * Mapping for /, depends on user where it starts.
     * @return Model and view.
     */
    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        if (sessionUser.isAnonymous()) return new ModelAndView("redirect:/welcome");
        if (sessionUser.isInvestor()) return new ModelAndView("redirect:/projects");
        return new ModelAndView("redirect:/messages");
    }

    /**
     * Login page view. Only anonymous user.
     * @return Model and view.x
     */
    @RequestMapping(value = "/login")
    public ModelAndView login(@RequestParam(name = "me", required = false) Integer message) {
        if (!sessionUser.isAnonymous()) return new ModelAndView("redirect:/");
        final ModelAndView modelAndView = new ModelAndView("index/login");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    /**
     * Welcome page view.
     * @return Model and view.
     */
    @RequestMapping(value = "/welcome")
    public ModelAndView welcome(){
        return new ModelAndView("index/welcome");
    }

    /**
     * Page header mapping. Used in all pages.
     * @return Model and view.
     */
    @RequestMapping(value = "/header")
    public ModelAndView headerComponent() {
        return new ModelAndView("components/header");
    }
}
