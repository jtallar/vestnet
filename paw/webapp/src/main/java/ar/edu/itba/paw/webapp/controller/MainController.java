package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.token.TokenGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    protected SessionUserFacade sessionUser;


    /**
     * Mapping for /, depends on user where it starts.
     * @return Model and view.
     */
    @RequestMapping("/")
    public ModelAndView index(){
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

    @RequestMapping(value = "/requestPassword")
    public ModelAndView requestPassword(@RequestParam(name = "error", defaultValue = "false") boolean error,
                                        @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent,
                                        @RequestParam(name = "invalidToken", defaultValue = "false") boolean invalidToken) {
        final ModelAndView mav = new ModelAndView("index/requestPassword");
        mav.addObject("error", error);
        mav.addObject("mailSent", mailSent);
        mav.addObject("invalidToken", invalidToken);
        return mav;
    }

    @RequestMapping(value = "/requestPassword", method = {RequestMethod.POST})
    public ModelAndView requestPassword(@RequestParam(name = "username") String email,
                                        HttpServletRequest request) throws MessagingException {
        Optional<User> maybeUser = userService.findByUsername(email);
        if (!maybeUser.isPresent()) return requestPassword(true, false, false);

        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        int token;
        try {
            token = TokenGeneratorUtil.getToken(maybeUser.get().getEmail() + maybeUser.get().getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Failed to generate token");
            return requestPassword(true, false, false);
        }
        emailService.sendPasswordRecovery(maybeUser.get(), String.valueOf(token), baseUrl);
        return requestPassword(false, true, false);
    }
}
