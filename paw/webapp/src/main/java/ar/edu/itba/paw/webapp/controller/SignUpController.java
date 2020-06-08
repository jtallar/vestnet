package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.event.PasswordRecoveryEvent;
import ar.edu.itba.paw.webapp.event.VerificationEvent;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.forms.NewPasswordFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
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
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected SessionUserFacade sessionUser;


    /**
     * Sign up view page mapping.
     * @param userFields The for fields to be filled.
     * @param invalidUser If there is an invalid user error.
     * @return Model and view.
     */
    @RequestMapping(value = "/signUp")
    public ModelAndView signUp(@ModelAttribute("userForm") final NewUserFields userFields,
                               @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser) {

        if (!sessionUser.isAnonymous()) return new ModelAndView("redirect:/");
        final ModelAndView mav = new ModelAndView("index/signUp");
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        mav.addObject("maxYear", LocalDate.now().getYear() - 18);
        mav.addObject("invalidUser", invalidUser);
        return mav;
    }


    /**
     * Maps the submitted form for new user.
     * @param userFields The completed user fields.
     * @param errors Errors.
     * @param request Used for auto login after sign up.
     * @return Model and view.
     */
    @RequestMapping(value = "/signUp", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields,
                               final BindingResult errors,
                               HttpServletRequest request)  {

        if(errors.hasErrors()) return logFormErrorsAndReturn(errors, "Sign Up", signUp(userFields, false));

        try {
            User newUser = userService.create(userFields.getRole(), userFields.getPassword(), userFields.getFirstName(), userFields.getLastName(),
                    userFields.getRealId(), userFields.getYear(), userFields.getMonth(), userFields.getDay(),
                    userFields.getCountry(), userFields.getState(),userFields.getCity(),
                    userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), userFields.getProfilePicture().getBytes());

            eventPublisher.publishEvent(new VerificationEvent(newUser, getBaseUrl(request)));
        } catch (UserAlreadyExistsException e) {
            LOGGER.error("User already exists with email {} in VestNet", userFields.getEmail());
            return signUp(userFields, true);
        } catch (IOException e) {
            return signUp(userFields, false);
        }
        return new ModelAndView("redirect:/login" + "?me=1");
    }


    /**
     * Gets the view for a password recovery.
     * @param error If there is no user with such email.
     * @return Model and view.
     */
    @RequestMapping(value = "/requestPassword", method = {RequestMethod.GET})
    public ModelAndView requestPassword(@RequestParam(name = "error", defaultValue = "false") boolean error ) {

        final ModelAndView mav = new ModelAndView("index/requestPassword");
        mav.addObject("error", error);
        return mav;
    }


    /**
     * Maps the posted form for a password recovery.
     * @param email The username email to make the recover.
     * @param request Http request.
     * @return Model and view.
     */
    @RequestMapping(value = "/requestPassword", method = {RequestMethod.POST})
    public ModelAndView requestPassword(@RequestParam(name = "username") String email,
                                        HttpServletRequest request) {

        Optional<User> maybeUser = userService.findByUsername(email);
        if (!maybeUser.isPresent()) return requestPassword(true);

        eventPublisher.publishEvent(new PasswordRecoveryEvent(maybeUser.get(), getBaseUrl(request)));
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
            eventPublisher.publishEvent(new VerificationEvent(optionalToken.get().getUser(), getBaseUrl(request)));
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
        return request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
    }
}