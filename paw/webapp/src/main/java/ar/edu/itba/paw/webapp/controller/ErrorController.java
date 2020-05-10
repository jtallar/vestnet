package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    @RequestMapping(value = "/errors")
    public ModelAndView errorPage(HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorCode", getErrorCode(httpServletRequest));
        return mv;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
}
