package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jason on 11/23/16.
 */
public class DOMCrossSiteScripting extends Assignment {
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    AttackResult completed(@RequestParam Integer param1,
                           @RequestParam Integer param2, HttpServletRequest request)
            throws IOException {
        
        if (param1 == 42 && param2 == 24 && request.getHeader("webgoat-requested-by").equals("dom-xss-vuln")) {
            return trackProgress(AttackResult.success("well done!"));
        } else {
            return trackProgress(AttackResult.failed("keep trying!"));
        }
    }

    @Override
    public String getPath() {
        return "/CrossSiteScripting/dom-xss";
    }
}





