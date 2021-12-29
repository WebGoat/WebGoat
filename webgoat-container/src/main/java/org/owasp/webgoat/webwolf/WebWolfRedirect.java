package org.owasp.webgoat.webwolf;

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
        var host = applicationContext.getEnvironment().getProperty("webwolf.host");
        var port = applicationContext.getEnvironment().getProperty("webwolf.port");

        return new ModelAndView("redirect:http://" + host + ":" + port + "/WebWolf/home");
    }
}
