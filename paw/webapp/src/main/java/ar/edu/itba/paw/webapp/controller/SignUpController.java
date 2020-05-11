package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.cookie.CookieUtil;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
public class SignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private UserService userService;

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
                               HttpServletRequest request, HttpServletResponse response){
        if(errors.hasErrors()){
            LOGGER.error("Sign Up failed. There are {} errors\n", errors.getErrorCount());
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
            // Escaping from potential XSS code insertions
            String firstName = StringEscapeUtils.escapeHtml4(userFields.getFirstName()), lastName = StringEscapeUtils.escapeHtml4(userFields.getLastName()),
                    id = StringEscapeUtils.escapeHtml4(userFields.getRealId()), email =  StringEscapeUtils.escapeHtml4(userFields.getEmail()),
                    phone = StringEscapeUtils.escapeHtml4(userFields.getPhone()), linkedin = StringEscapeUtils.escapeHtml4(userFields.getLinkedin());

            userId = userService.create(userFields.getRole(), firstName, lastName, id,
                    LocalDate.of(userFields.getYear(), userFields.getMonth(), userFields.getDay()),
                    new Location(new Location.Country(userFields.getCountry(), "", "", "", "", ""),
                            new Location.State(userFields.getState(), "", ""),
                            new Location.City(userFields.getCity(), "")),
                    email, phone, linkedin, userFields.getPassword(), imageBytes);
        } catch (UserAlreadyExistsException e) {
            return signUp(userFields, true);
        }

        // Auto Log In
        authenticateUserAndSetSession(StringEscapeUtils.escapeHtml4(userFields.getEmail()), userFields.getPassword(), request, response);
        return new ModelAndView("redirect:/");
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

}
