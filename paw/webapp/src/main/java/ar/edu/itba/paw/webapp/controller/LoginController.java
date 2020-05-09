package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.webapp.config.RoleCookieSuccessHandler;
import ar.edu.itba.paw.webapp.config.WebConfig;
import ar.edu.itba.paw.webapp.cookie.CookieUtil;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login")
    public ModelAndView login(){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ANONYMOUS"))
            return new ModelAndView("redirect:/");
        final ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @RequestMapping(value = "/welcome")
    public ModelAndView welcome(){
        final ModelAndView mav = new ModelAndView("welcome");
        return mav;
    }

    @RequestMapping(value = "/signUp")
    public ModelAndView signUp(@ModelAttribute("userForm") final NewUserFields userFields,
                               @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ANONYMOUS"))
            return new ModelAndView("redirect:/");
        final ModelAndView mav = new ModelAndView("signUp");
        mav.addObject("maxSize", WebConfig.MAX_UPLOAD_SIZE);
        mav.addObject("invalidUser", invalidUser);
        return mav;
    }

    @RequestMapping(value = "/signUp", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields,
                               final BindingResult errors, @RequestParam(name = "invalidUser", defaultValue = "false") boolean invalidUser,
                               HttpServletRequest request, HttpServletResponse response){
        if(errors.hasErrors()){
            LOGGER.debug("\n\nSign Up failed. There are {} errors\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.debug("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            LOGGER.debug("\n\n");
            return signUp(userFields, false);
        }
        byte[] imageBytes = new byte[0];
        try {
            if (!userFields.getProfilePicture().isEmpty())
                imageBytes = userFields.getProfilePicture().getBytes();
        } catch (IOException e) {
            return signUp(userFields, false);
        }

        //TODO add location when working
        final long userId;
        try {
            // Escaping from potential XSS code insertions
            String firstName = StringEscapeUtils.escapeHtml4(userFields.getFirstName()), lastName = StringEscapeUtils.escapeHtml4(userFields.getLastName()),
                    id = StringEscapeUtils.escapeHtml4(userFields.getRealId()), email =  StringEscapeUtils.escapeHtml4(userFields.getEmail()),
                    phone = StringEscapeUtils.escapeHtml4(userFields.getPhone()), linkedin = StringEscapeUtils.escapeHtml4(userFields.getLinkedin());

            userId = userService.create(userFields.getRole(), firstName, lastName, id,
                    LocalDate.of(userFields.getYear(), userFields.getMonth(), userFields.getDay()),
                    //                new Location(userFields.getCountry(), userFields.getState(), userFields.getCity()),
                    new Location(new Location.Country(userFields.getCountry(), "", "", "", ""),
                            new Location.State(userFields.getState(), "", ""), new Location.City(userFields.getCity(), "")),
                   email, phone, linkedin, userFields.getPassword(), imageBytes);
        } catch (UserAlreadyExistsException e) {
            return signUp(userFields, true);
        }

        // Auto Log In
        authenticateUserAndSetSession(StringEscapeUtils.escapeHtml4(userFields.getEmail()), userFields.getPassword(), request, response);
        return new ModelAndView("redirect:/");
    }

    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        CookieUtil.generateRoleCookie(response, authenticatedUser);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }


    @RequestMapping(value = "/location/country",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Location.Country> countryList() {
        return locationService.findAllCountries();
    }

    @RequestMapping(value = "/location/state/{country_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Location.State> stateList(@PathVariable("country_id") long countryId) {
        return locationService.findStates(countryId);
    }

    @RequestMapping(value = "/location/city/{state_id}",  headers = "accept=application/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Location.City> cityList(@PathVariable("state_id") long stateId) {
        return locationService.findCities(stateId);
    }


}
