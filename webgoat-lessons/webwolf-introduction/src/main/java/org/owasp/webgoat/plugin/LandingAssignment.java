package org.owasp.webgoat.plugin;

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@AssignmentPath("/WebWolf/landing")
public class LandingAssignment extends AssignmentEndpoint {

    @Value("${webwolf.url.landingpage}")
    private String landingPageUrl;

    @PostMapping
    @ResponseBody
    public AttackResult click(String uniqueCode) {
        if (StringUtils.reverse(getWebSession().getUserName()).equals(uniqueCode)) {
            return trackProgress(success().build());
        }
        return failed().feedback("webwolf.landing_wrong").build();
    }


    @GetMapping("/password-reset")
    public ModelAndView openPasswordReset(HttpServletRequest request) throws URISyntaxException {
        URI uri = new URI(request.getRequestURL().toString());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("webwolfUrl", landingPageUrl);
        modelAndView.addObject("uniqueCode", StringUtils.reverse(getWebSession().getUserName()));

        modelAndView.setViewName("webwolfPasswordReset");
        return modelAndView;
    }


}
