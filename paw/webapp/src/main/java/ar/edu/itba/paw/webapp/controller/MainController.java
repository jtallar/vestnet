package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.forms.NewPasswordFields;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    /**
     * Session user data.
     * @return The logged in user.
     */
    @ModelAttribute("sessionUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("\n\n loggedUser() called\n\n");
        if(auth != null)
            return userService.findByUsername(auth.getName()).orElse(null);
        return null;
    }

    /**
     * Mapping for /, depends on user where it starts.
     * @return Model and view.
     */
    @RequestMapping("/")
    public ModelAndView index(){
        User user = loggedUser();
        if (user == null)
            return new ModelAndView("redirect:/welcome");
        if (user.getRole() == User.UserRole.ENTREPRENEUR.getId())
            return new ModelAndView("redirect:/messages");
        return new ModelAndView("redirect:/projects");
    }

    /**
     * Login page view. Only anonymous user.
     * @return Model and view.
     */
    @RequestMapping(value = "/login")
    public ModelAndView login(){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ANONYMOUS"))
            return new ModelAndView("redirect:/");
        return new ModelAndView("index/login");
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
                                        @RequestParam(name = "mailSent", defaultValue = "false") boolean mailSent) {
        final ModelAndView mav = new ModelAndView("index/requestPassword");
        mav.addObject("error", error);
        mav.addObject("mailSent", mailSent);
        return mav;
    }

    @RequestMapping(value = "/requestPassword", method = {RequestMethod.POST})
    public ModelAndView requestPassword(@RequestParam(name = "username") String email, HttpServletRequest request) throws MessagingException {
        LOGGER.debug("\n\nEl email recibido es {}\n\n", email);
        Optional<User> maybeUser = userService.findByUsername(StringEscapeUtils.escapeXml11(email));
        if (!maybeUser.isPresent()) {
            return requestPassword(true, false);
        }
        String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())) + request.getContextPath();
        emailService.sendPasswordRecovery(maybeUser.get(), baseUrl);
        return requestPassword(false, true);
    }
}
