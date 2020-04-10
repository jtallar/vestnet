package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.ProjectCategories;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.mail.MailFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class HelloWorldController {

    //@Autowired
    //private UserService userService;

    //@Autowired
    //private ProjectService projectService;

    @Autowired
    private EmailService emailService;

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("404");
    }

    @RequestMapping(value = "/contact", method = {RequestMethod.GET})
    public ModelAndView contact(@ModelAttribute("mailForm") final MailFields mailFields) {
            return new ModelAndView("contact");
    }

    @RequestMapping(value = "/contact", method = {RequestMethod.POST})
    public ModelAndView contactPost(@Valid @ModelAttribute("mailForm") final MailFields mailFields){
           /* if (errors.hasErrors()) {
                return index(form);
            }*/
           emailService.sendNewEmail(mailFields.getFrom(), mailFields.getSubject(), mailFields.getBody());
           return new ModelAndView("redirect:/emailSuccess");
    }

    @RequestMapping("/emailSuccess")
    public ModelAndView emailSuccess(){
        return new ModelAndView("mainView");        // success jsp
    }

    /*
    @RequestMapping("/{id}")
    public ModelAndView helloWorld(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/main")
    public ModelAndView mainView() {
        final ModelAndView mav = new ModelAndView("mainView");
        mav.addObject("list", projectService.findAllProjects());
        List<ProjectCategories> catList = Arrays.asList(ProjectCategories.class.getEnumConstants());
        mav.addObject("cat", catList);
        return mav;
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView register(@RequestParam(name = "username", required = true) String username) {
        final User user = userService.create(username);
        return new ModelAndView("redirect:/" + user.getId());
    }

     */
}
