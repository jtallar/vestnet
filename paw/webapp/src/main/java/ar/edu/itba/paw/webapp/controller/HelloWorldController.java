package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
public class HelloWorldController {
    /*@Autowired
    private UserService userService;*/

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("404");
    }

    /*@RequestMapping("/{id}")
    public ModelAndView helloWorld(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }*/

    @RequestMapping(value = "/main")
    public ModelAndView mainView() {
        final ModelAndView mav = new ModelAndView("mainView");
        return mav;
    }

    private static final String SUMMARY = "Este es un gran proyecto de prubea. Como podemos ver, tiene un gran titulo, un gran autor y un gran sumario. El owner ID y el ID " +
            "del proyecto estan completamente de mas. Simplemente estan para ver que se pase bien desde el HelloWorldController, que no se por que se sigue llamando asi," +
            "al singleProjectView.jsp. La idea es poner nombres lo mas largos posibles para que nadie pueda llegar a repetirlo. Le iba a poner algo como" +
            "singleProjectViewHechoPorJulianTallarElLunes.jsp pero era demasiado largo. Veremos como queda todo este texto.";

    @RequestMapping(value = "/projects/{id}")
    public ModelAndView singleProjectView(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("singleProjectView");
        mav.addObject("project", new Project(id, "Proyecto de prueba 1", SUMMARY, 1, null));
        mav.addObject("owner", "Julian Tallar");
        return mav;
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView register(@RequestParam(name = "username", required = true) String username) {
        final User user = userService.create(username);
        return new ModelAndView("redirect:/" + user.getId());
    }*/
}
