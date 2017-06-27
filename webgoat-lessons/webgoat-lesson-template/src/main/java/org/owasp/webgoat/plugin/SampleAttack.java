package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

/**
 * Created by jason on 1/5/17.
 */

@AssignmentPath("/lesson-template/sample-attack")
public class SampleAttack extends AssignmentEndpoint {

    String secretValue = "secr37Value";

    //UserSessionData is bound to session and can be used to persist data across multiple assignments
    @Autowired
    UserSessionData userSessionData;


    @GetMapping(produces = {"application/json"})
    public @ResponseBody
    AttackResult completed(String param1, String param2, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (userSessionData.getValue("some-value") != null) {
            // do any session updating you want here ... or not, just comment/example here
            //return trackProgress(failed().feedback("lesson-template.sample-attack.failure-2").build());
        }

        //overly simple example for success. See other existing lesssons for ways to detect 'success' or 'failure'
        if (secretValue.equals(param1)) {
            return trackProgress(success()
                    .output("Custom Output ...if you want, for success")
                    .feedback("lesson-template.sample-attack.success")
                    .build());
            //lesson-template.sample-attack.success is defined in src/main/resources/i18n/WebGoatLabels.properties
        }

        // else
        return trackProgress(failed()
                .feedback("lesson-template.sample-attack.failure-2")
                .output("Custom output for this failure scenario, usually html that will get rendered directly ... yes, you can self-xss if you want")
                .build());
    }

}
