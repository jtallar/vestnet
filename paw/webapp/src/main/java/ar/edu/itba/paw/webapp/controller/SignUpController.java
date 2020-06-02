package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.SessionUserFacade;
import ar.edu.itba.paw.interfaces.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.component.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.forms.NewPasswordFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import ar.edu.itba.paw.webapp.token.TokenGeneratorUtil;
import org.apache.commons.text.StringEscapeUtils;
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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

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
                               HttpServletResponse response) throws MessagingException {

        // TODO check how to handle form errors, exception or not
        if(errors.hasErrors()) return logFormErrorsAndReturn(errors, "Sign Up", signUp(userFields, false));

        byte[] imageBytes = new byte[0];
        User newUser;
        try {
            if (!userFields.getProfilePicture().isEmpty()) imageBytes = userFields.getProfilePicture().getBytes();

            newUser= userService.create(userFields.getRole(), userFields.getPassword(), userFields.getFirstName(), userFields.getLastName(),
                    userFields.getRealId(), userFields.getYear(), userFields.getMonth(), userFields.getDay(),
                    userFields.getCountry(), userFields.getState(),userFields.getCity(),
                    userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), imageBytes);

            // TODO finish this
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser, request.getLocale(), ""));

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
                                        HttpServletRequest request) throws MessagingException {

        // TODO check if solve with exceptions
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

    @RequestMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("passwordForm") final NewPasswordFields passwordFields,
                                      @RequestParam(name = "username") String email,
                                      @RequestParam(name = "token") int token) {
        String decodedEmail = new String(Base64.getUrlDecoder().decode(StringEscapeUtils.escapeXml11(email).getBytes()));

        // TODO check if solve with exceptions
        Optional<User> maybeUser = userService.findByUsername(decodedEmail);
        if (!maybeUser.isPresent())
            return new ModelAndView("redirect:/login");
        try {
            if (!TokenGeneratorUtil.checkToken(maybeUser.get().getEmail() + maybeUser.get().getPassword(), token)) {
                LOGGER.warn("\n\nToken Expired\n\n");
                return new ModelAndView("redirect:/requestPassword?invalidToken=1");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Failed to check token");
            return new ModelAndView("redirect:/login");
        }
        final ModelAndView mav = new ModelAndView("index/resetPassword");
        mav.addObject("email", decodedEmail);
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST})
    public ModelAndView resetPassword(@Valid @ModelAttribute("passwordForm") final NewPasswordFields passwordFields,
                                      final BindingResult errors,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        if (errors.hasErrors()) return resetPassword(passwordFields, passwordFields.getEmail(), passwordFields.getToken());

        // TODO do not forget this
//        userService.updateUserPassword(passwordFields.getEmail(), passwordFields.getPassword());
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/verify")
    public ModelAndView resetPassword(@RequestParam(name = "username") String email,
                                      @RequestParam(name = "token") int token,
                                      HttpServletRequest request) throws MessagingException {
        String decodedEmail = new String(Base64.getUrlDecoder().decode(StringEscapeUtils.escapeXml11(email).getBytes()));
        Optional<User> maybeUser = userService.findByUsername(decodedEmail);
        if (!maybeUser.isPresent())
            return new ModelAndView("redirect:/login?me=2");
        try {
            if (!TokenGeneratorUtil.checkToken(maybeUser.get().getEmail() + maybeUser.get().getPassword(), token)) {
                LOGGER.warn("\n\nToken Expired\n\n");
//                sendVerificationMail(maybeUser.get().getId(), request);
                return new ModelAndView("redirect:/login?me=3");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Failed to check token");
            return new ModelAndView("redirect:/login?me=2");
        }

        // TODO do not forget this
//        userService.verifyUser(decodedEmail);
        return new ModelAndView("redirect:/login?me=4");
    }

    private ModelAndView logFormErrorsAndReturn(BindingResult errors, String formName, ModelAndView modelAndView) {
            LOGGER.error(formName + " failed. There are {} errors in form\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            return modelAndView;
    }
}