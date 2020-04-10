package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.CategoriesDao;
import ar.edu.itba.paw.interfaces.CategoriesService;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.ProjectCategories;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class HelloWorldController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CategoriesService categoriesService;

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("404");
    }


    @RequestMapping("/{id}")
    public ModelAndView helloWorld(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        List<Category> catList = categoriesService.findAllCats();
        mav.addObject("cats", catList);
        return mav;
    }

    @RequestMapping(value = "/main")
    public ModelAndView mainView() {
        final ModelAndView mav = new ModelAndView("mainView");
        mav.addObject("list", projectService.findAll());
        List<ProjectCategories> catList = Arrays.asList(ProjectCategories.class.getEnumConstants());
        mav.addObject("cat", catList);
        return mav;
    }

    /*@RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView register(@RequestParam(name = "username", required = true) String username) {
        final User user = userService.create(username);
        return new ModelAndView("redirect:/" + user.getId());
    }*/

}
