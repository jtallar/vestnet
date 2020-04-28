package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.webapp.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.webapp.forms.NewUserFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);
    @Autowired
    private UserService userService;

    @Autowired
    protected AuthenticationManager authenticationManager;


    @RequestMapping(value = "/login")
    public ModelAndView login(){
        final ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @RequestMapping(value = "/signUp", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("userForm") final NewUserFields userFields, final BindingResult errors, HttpServletRequest request){
        if(errors.hasErrors()){
            LOGGER.debug("\n\nSign Up failed. There are {} errors\n", errors.getErrorCount());
            for (ObjectError error : errors.getAllErrors())
                LOGGER.debug("\nName: {}, Code: {}", error.getDefaultMessage(), error.toString());
            LOGGER.debug("\n\n");
            return signUp(userFields);
        }
        byte[] imageBytes = new byte[0];
        try {
            if (!userFields.getProfilePicture().isEmpty())
                imageBytes = userFields.getProfilePicture().getBytes();
        } catch (IOException e) {
            return signUp(userFields);
        }

        //TODO add location when working
        final long userId = userService.create(userFields.getRole(), userFields.getFirstName(), userFields.getLastName(), userFields.getRealId(),
                LocalDate.of(userFields.getYear(), userFields.getMonth(), userFields.getDay()),
//                new Location(userFields.getCountry(), userFields.getState(), userFields.getCity()),
                new Location(new Location.Country(userFields.getCountry(), "", "", "", ""),
                        new Location.State(userFields.getState(), "", ""), new Location.City(userFields.getCity(), "")),
                userFields.getEmail(), userFields.getPhone(), userFields.getLinkedin(), userFields.getPassword(), imageBytes);
//        userService.createPassword(user.getId(), userFields.getPassword());
        if (userId <= 0) {
            // TODO: VER SI MOSTRAMOS LA MISMA PAG DE ERROR --> EL header tiene cosaas
            throw new UserAlreadyExistsException();
        }

        // Auto Log In
        authenticateUserAndSetSession(userFields.getEmail(), userFields.getPassword(), request);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/signUp")
    public ModelAndView signUp( @ModelAttribute("userForm") final NewUserFields userFields){
        final ModelAndView mav = new ModelAndView("signUp");
        return mav;
    }

    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

}
