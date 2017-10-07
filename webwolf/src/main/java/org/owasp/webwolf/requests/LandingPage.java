package org.owasp.webwolf.requests;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping("/landing/**")
public class LandingPage {

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.OK)
    public void ok(HttpServletRequest request) {
      log.trace("Incoming request for: {}", request.getRequestURL());
    }

}
