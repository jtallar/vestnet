package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.cookie.CookieUtil;
import ar.edu.itba.paw.webapp.forms.NewPasswordFields;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import ar.edu.itba.paw.webapp.token.TokenGeneratorUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    /**
     * Sign up view page mapping.
     * @param userFields The for fields to be filled.
     * @param invalidUser If there is an invalid user error.
     * @return Model and view.
     */
    @RequestMapping(value = "/signUp")
    public ModelAndView signUp(@ModelAttribute("userForm") final NewUserFields userFields,
                               @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ANONYMOUS"))
            return new ModelAndView("redirect:/");
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
                               final BindingResult errors, @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser,
                               HttpServletRequest request, HttpServletResponse response) throws MessagingException {
        if(errors.hasErrors()){
            LOGGER.error("Sign Up failed. There are {} errors in form\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.error("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            return signUp(userFields, false);
        }

        byte[] imageBytes = new byte[0];
        try {
            if (!userFields.getProfilePicture().isEmpty())
                imageBytes = userFields.getProfilePicture().getBytes();
        } catch (IOException e) {
            return signUp(userFields, false);
        }

        final long userId;
        try {
            userId = userService.create(userFields.getRole(), userFields.getFirstName(), userFields.getLastName(), userFields.getRealId(),
                    LocalDate.of(userFields.getYear(), userFields.getMonth(), userFields.getDay()),
                    new Location(new Location.Country(userFields.getCountry(), "", "", "", "", ""),
                            new Location.State(userFields.getState(), "", ""),
                            new Location.City(userFields.getCity(), "")),
                    userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), userFields.getPassword(), imageBytes);
        } catch (UserAlreadyExistsException e) {
            LOGGER.error("User already exists with email {} in VestNet", userFields.getEmail());
            return signUp(userFields, true);
        }

        sendVerificationMail(userId, request);
        return new ModelAndView("redirect:/login?me=1");
    }

    @RequestMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("passwordForm") final NewPasswordFields passwordFields,
                                      @RequestParam(name = "username") String email, @RequestParam(name = "token") int token) {
        String decodedEmail = new String(Base64.getUrlDecoder().decode(StringEscapeUtils.escapeXml11(email).getBytes()));
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
                                      final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return resetPassword(passwordFields, passwordFields.getEmail(), passwordFields.getToken());
        }
        String password = StringEscapeUtils.escapeXml11(passwordFields.getPassword());
        userService.updateUserPassword(passwordFields.getEmail(), password);

        // Auto Log In
        authenticateUserAndSetSession(passwordFields.getEmail(), password, request, response);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/verify")
    public ModelAndView resetPassword(@RequestParam(name = "username") String email,
                                      @RequestParam(name = "token") int token, HttpServletRequest request) throws MessagingException {
        String decodedEmail = new String(Base64.getUrlDecoder().decode(StringEscapeUtils.escapeXml11(email).getBytes()));
        Optional<User> maybeUser = userService.findByUsername(decodedEmail);
        if (!maybeUser.isPresent())
            return new ModelAndView("redirect:/login?me=2");
        try {
            if (!TokenGeneratorUtil.checkToken(maybeUser.get().getEmail() + maybeUser.get().getPassword(), token)) {
                LOGGER.warn("\n\nToken Expired\n\n");
                sendVerificationMail(maybeUser.get().getId(), request);
                return new ModelAndView("redirect:/login?me=3");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Failed to check token");
            return new ModelAndView("redirect:/login?me=2");
        }
        userService.verifyUser(decodedEmail);
        return new ModelAndView("redirect:/login?me=4");
    }


    /**
     *  Auxiliary functions
     */

    /**
     * Authenticates user and set its session for automatic login.
     * @param username The user's name.
     * @param password The user's password.
     * @param request The http request.
     * @param response The http response.
     */
    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        CookieUtil.generateRoleCookie(response, authenticatedUser);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    /**
     * Sends the verification email
     * @param userId The unique user id
     * @param request The http request
     * @throws MessagingException
     */
    private void sendVerificationMail(long userId, HttpServletRequest request) throws MessagingException {
        Optional<User> maybeUser = userService.findById(userId);
        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        int token = 0;
        try {
            token = TokenGeneratorUtil.getToken(maybeUser.get().getEmail() + maybeUser.get().getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Failed to generate token");
        }
        emailService.sendVerificationEmail(userService.findById(userId).get(), String.valueOf(token), baseUrl);
    }

}
