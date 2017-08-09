package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@AssignmentPath("/access-control/hidden-menu")
@AssignmentHints({"access-control.hidden-menus.hint1","access-control.hidden-menus.hint2","access-control.hidden-menus.hint3"})
public class MissingFunctionACHiddenMenus extends AssignmentEndpoint {
    //UserSessionData is bound to session and can be used to persist data across multiple assignments
    @Autowired
    UserSessionData userSessionData;


    @PostMapping(produces = {"application/json"})
    public @ResponseBody
    AttackResult completed(String hiddenMenu1, String hiddenMenu2, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //overly simple example for success. See other existing lesssons for ways to detect 'success' or 'failure'
        if (hiddenMenu1.equals("Users") && hiddenMenu2.equals("Config")) {
            return trackProgress(success()
                    .output("")
                    .feedback("access-control.hidden-menus.success")
                    .build());
        }

        if (hiddenMenu1.equals("Config") && hiddenMenu2.equals("Users")) {
            return trackProgress(failed()
                    .output("")
                    .feedback("access-control.hidden-menus.close")
                    .build());
        }

        return trackProgress(failed()
                .feedback("access-control.hidden-menus.failure")
                .output("")
                .build());
    }

}
