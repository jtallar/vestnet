package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.CategoriesService;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.webapp.exception.ProjectNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.mail.MailFields;
import ar.edu.itba.paw.webapp.forms.CategoryFilter;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

@Controller
public class HelloWorldController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CategoriesService categoriesService;

    @ExceptionHandler({UserNotFoundException.class, ProjectNotFoundException.class})
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

    @RequestMapping("/{id}")
    public ModelAndView helloWorld(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("cats", catList);
        return mav;
    }

    @RequestMapping(value = "/projects")
    public ModelAndView mainView(@ModelAttribute("categoryForm")CategoryFilter catFilter) {
        final ModelAndView mav = new ModelAndView("mainView");

        // TODO: CAMBIARLO A findAllCats cuando sepamos que onda lo de locale obtenido de la BD
        List<Project> projectList = new ArrayList<>();
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("cats", catList);

        if (catFilter.getCategorySelector() != null) {
            Optional<Category> selectedCategory = catList.stream()
                    .filter(category -> category.getName().equals(catFilter.getCategorySelector()))
                    .findFirst();
            if (selectedCategory.isPresent()) {
                projectList = projectService.findByCategories(Collections.singletonList(selectedCategory.get()));
            }
        } else {
            projectList = projectService.findAll();
        }


//        List<Project> projectList = projectService.findAll();
//        List<Project> toRemove = new ArrayList<>();
//        if (catFilter.getCategorySelector() != null){
//            projectList.forEach(project -> {
//                if(!project.hasCategory(catFilter.getCategorySelector())){
//
//                    toRemove.add(project);
//                }
//            });
//        }
//
//        toRemove.forEach(project -> {
//            projectList.remove(project);
//        });




        mav.addObject("list", projectList);

        return mav;
    }

    // TODO> COMO LE PASO EL PROJECT CLICKEADO POR PARAMS A ESTE? ASI TENGO QUE IR DE NUEVO A LA BD
    @RequestMapping(value = "/projects/{id}")
    public ModelAndView singleProjectView(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", projectService.findById(id).orElseThrow(ProjectNotFoundException::new));
        return mav;
    }

    /*@RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView register(@RequestParam(name = "username", required = true) String username) {
        final User user = userService.create(username);
        return new ModelAndView("redirect:/" + user.getId());
    }*/

}
