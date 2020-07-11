package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.forms.MailFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ProjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    /** This are constant for now */
    private static final int PAGE_SIZE = 12;
    private static final int PAGINATION_ITEMS = 5;


    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @Autowired
    protected SessionUserFacade sessionUser;



    /**
     * Single project view page.
     * @param mailFields Fields for mail contact
     * @param id The unique project id.
     * @param sent Boolean if mail was sent.
     * @return Model and view.
     */
    @RequestMapping(value = "/projects/{id}", method = {RequestMethod.GET})
    public ModelAndView singleProjectView(@Valid @ModelAttribute("mailForm") final MailFields mailFields,
                                          @PathVariable("id") long id,
                                          @RequestParam(name = "sent", defaultValue = "false") boolean sent) {

        final ModelAndView mav = new ModelAndView("project/singleView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        mav.addObject("sent", sent);
        if (sessionUser.isInvestor()) {
            mav.addObject("user", userService.findById(sessionUser.getId()).orElseThrow(UserNotFoundException::new));
            mav.addObject("lastMessage", userService.getLastProjectOfferMessage(sessionUser.getId(), id));
        }
        return mav;
    }



}
