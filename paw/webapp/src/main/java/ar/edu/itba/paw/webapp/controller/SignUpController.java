package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.forms.NewPasswordFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected SessionUserFacade sessionUser;


    /**
     * Maps the posted form for a password recovery.
     * @param email The username email to make the recover.
     * @param request Http request.
     * @return Model and view.
     */
    @RequestMapping(value = "/requestPassword", method = {RequestMethod.POST})
    public ModelAndView requestPassword(@RequestParam(name = "username") String email,
                                        HttpServletRequest request) {

        Optional<User> maybeUser = userService.findByUsername(email); // TODO migrate to rest
        // TODO password event publisher removed. It did:
        // User user = event.getUser();
        // String token = userService.createToken(user.getId()).getToken();
        // emailService.sendPasswordRecovery(user, token, event.getBaseUrl());
        return new ModelAndView("redirect:/login" + "?me=3");
    }


    /**
     * Reset password form view, checks token.
     * @param form The used form for a new password.
     * @param token The token given.
     * @return Model and view.
     */
    @RequestMapping(value = "/resetPassword", method = {RequestMethod.GET})
    public ModelAndView resetPassword(@ModelAttribute("passwordForm") final NewPasswordFields form,
                                      @RequestParam(name = "token") String token) {

        if (!sessionUser.isAnonymous()) return new ModelAndView("redirect:/");
        Optional<Token> optionalToken = userService.findToken(token);
        if (!optionalToken.isPresent()) return new ModelAndView("redirect:/login" + "?me=12");
        if (!optionalToken.get().isValid()) return new ModelAndView("redirect:/login" + "?me=13");

        final ModelAndView mav = new ModelAndView("index/resetPassword");
        mav.addObject("email", optionalToken.get().getUser().getEmail());
        mav.addObject("token", token);
        return mav;
    }


    /**
     * Post model for the password recovery form.
     * @param form Form with the new password.
     * @param errors Errors from the form.
     * @return Model and view.
     */
    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST})
    public ModelAndView resetPassword(@Valid @ModelAttribute("passwordForm") final NewPasswordFields form,
                                      final BindingResult errors ) {

        if (errors.hasErrors()) return logFormErrorsAndReturn(errors, "Reset Password", resetPassword(form, form.getToken()));

        userService.updatePassword(form.getEmail(), form.getPassword());
        return new ModelAndView("redirect:/login" + "?me=4");
    }


    /**
     * Verify view page, checks token and verifies user.
     * @param token Token to check.
     * @param request Http request.
     * @return Model and view.
     */
    @RequestMapping(value = "/verify")
    public ModelAndView verifyUser(@RequestParam(name = "token") String token,
                                      HttpServletRequest request) {

        Optional<Token> optionalToken = userService.findToken(token);
        if (!optionalToken.isPresent()) return new ModelAndView("redirect:/login" + "?me=10");
        if (!optionalToken.get().isValid()) {
            // TODO verification vevent publisher removed. It did:
            // User user = event.getUser();
            // String token = userService.createToken(user.getId()).getToken();
            // emailService.sendVerification(user, token, event.getBaseUrl());
            return new ModelAndView("redirect:/login" + "?me=11");
        }

        userService.verifyUser(optionalToken.get().getUser().getId());
        return new ModelAndView("redirect:/login" + "?me=2");
    }


    /** Auxiliary functions (to reduce code) */


    /**
     * Logs form errors and returns the given model and view
     * @param errors The errors returned.
     * @param formName The form name used to generate the string.
     * @param modelAndView Model and view to return to.
     * @return To the given model and view.
     */
    private ModelAndView logFormErrorsAndReturn(BindingResult errors, String formName, ModelAndView modelAndView) {
            LOGGER.error(formName + " failed. There are {} errors in form\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            return modelAndView;
    }


    /**
     * Creates the base url needed.
     * @param request The given request to get the base url from.
     * @return Base url string formatted.
     */
    private String getBaseUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath()));
    }
}