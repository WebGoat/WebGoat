package org.owasp.webgoat.container;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class WebWolfRedirect {

    private final ApplicationContext applicationContext;

    @GetMapping("/WebWolf")
    public ModelAndView openWebWolf() {
        var url = applicationContext.getEnvironment().getProperty("webwolf.url");

        return new ModelAndView("redirect:" + url + "/home");
    }
}
