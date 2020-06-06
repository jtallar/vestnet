package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Token;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.TokenEventType;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.webapp.component.SendTokenEvent;
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
import javax.servlet.http.HttpServletResponse;
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
    private ImageService imageService;

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
     * @param invalidUser If there is an invalid user error.
     * @param request Used for auto login after sign up.
     * @param response Used for auto login after sign up.
     * @return Model and view.
     */
    @RequestMapping(value = "/signUp", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields,
                               final BindingResult errors,
                               @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser,
                               HttpServletRequest request,
                               HttpServletResponse response)  {

        // TODO check how to handle form errors, exception or not
        if(errors.hasErrors()) return logFormErrorsAndReturn(errors, "Sign Up", signUp(userFields, false));

        byte[] imageBytes = new byte[0];
        User newUser;
        try {
            if (!userFields.getProfilePicture().isEmpty()) imageBytes = userFields.getProfilePicture().getBytes();

            UserImage userImage = imageService.create(imageBytes);

            newUser = userService.create(userFields.getRole(), userFields.getPassword(), userFields.getFirstName(), userFields.getLastName(),
                    userFields.getRealId(), userFields.getYear(), userFields.getMonth(), userFields.getDay(),
                    userFields.getCountry(), userFields.getState(),userFields.getCity(),
                    userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), userImage.getId());

            eventPublisher.publishEvent(new SendTokenEvent(newUser, getBaseUrl(request), TokenEventType.USER_VERIFICATION));

        } catch (UserAlreadyExistsException e) {
            // TODO when user already exists
            LOGGER.error("User already exists with email {} in VestNet", userFields.getEmail());
            return signUp(userFields, true);
        } catch (IOException e) {
            // TODO when image conversion fails
            return signUp(userFields, false);
        } catch (RuntimeException e) {
            // TODO when mail fails
            return new ModelAndView("redirect:/login?me=1");
        }

        return new ModelAndView("redirect:/login?me=1");
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
                                        HttpServletRequest request) {

        // TODO check if solve with exceptions
        Optional<User> maybeUser = userService.findByUsername(email);
        if (!maybeUser.isPresent()) return requestPassword(true, false, false);

        try {
            eventPublisher.publishEvent(new SendTokenEvent(maybeUser.get(), getBaseUrl(request), TokenEventType.FORGOT_PASSWORD));
        } catch (RuntimeException e) {
            // TODO when mail fails
        }

        return requestPassword(false, true, false);
    }

    @RequestMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("passwordForm") final NewPasswordFields form,
                                      @RequestParam(name = "token") String token) {

        // TODO check if solve with exceptions
        Optional<Token> optionalToken = userService.findToken(token);
        if (!optionalToken.isPresent()) return new ModelAndView("redirect:/login");
        if (!optionalToken.get().isValid()) return new ModelAndView("redirect:/requestPassword?invalidToken=1");

        final ModelAndView mav = new ModelAndView("index/resetPassword");
        mav.addObject("email", optionalToken.get().getUser().getEmail());
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST})
    public ModelAndView resetPassword(@Valid @ModelAttribute("passwordForm") final NewPasswordFields form,
                                      final BindingResult errors,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        // TODO this is wrong, already checked and went to the DB
        if (errors.hasErrors()) return logFormErrorsAndReturn(errors, "Reset Pass", resetPassword(form, form.getToken()));

        userService.updatePassword(form.getEmail(), form.getPassword());
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/verify")
    public ModelAndView verifyUser(@RequestParam(name = "token") String token,
                                      HttpServletRequest request) {

        // TODO check if solve with exceptions
        Optional<Token> optionalToken = userService.findToken(token);
        if (!optionalToken.isPresent()) return new ModelAndView("redirect:/login?me=3");

        if (!optionalToken.get().isValid()) {
            try{
                eventPublisher.publishEvent(new SendTokenEvent(optionalToken.get().getUser(), getBaseUrl(request), TokenEventType.USER_VERIFICATION));
            } catch (RuntimeException e) {
                // TODO when mail fails
            }
            return new ModelAndView("redirect:/login?me=2");
        }

        userService.verifyUser(optionalToken.get().getUser().getId());
        return new ModelAndView("redirect:/login?me=4");
    }


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