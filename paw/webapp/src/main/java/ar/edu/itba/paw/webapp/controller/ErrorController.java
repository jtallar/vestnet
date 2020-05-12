package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping(value = "/errors")
    public ModelAndView errorPage(HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("error/error");
        int errorCode = getErrorCode(httpServletRequest);
        LOGGER.error("Error page loaded. Error code {}", errorCode);
        mv.addObject("errorCode", errorCode);
        return mv;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
}
