package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import org.jcodings.util.Hash;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

/**
 * Created by jason on 1/5/17.
 */

@AssignmentPath("/auth-bypass/verify-account")
@AssignmentHints({"auth-bypass.hints.verify.1", "auth-bypass.hints.verify.2", "auth-bypass.hints.verify.3", "auth-bypass.hints.verify.4"})
public class VerifyAccount extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;

    @Autowired
    UserSessionData userSessionData;

    @PostMapping(produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(@RequestParam String userId, @RequestParam String verifyMethod, HttpServletRequest req) throws ServletException, IOException {


        AccountVerificationHelper verificationHelper = new AccountVerificationHelper();
        Map<String,String> submittedAnswers = parseSecQuestions(req);
        if (verificationHelper.didUserLikelylCheat((HashMap)submittedAnswers)) {
            return trackProgress(failed()
            .feedback("verify-account.cheated")
            .output("Yes, you guessed correcctly,but see the feedback message")
            .build());
        }

        // else
        if (verificationHelper.verifyAccount(new Integer(userId),(HashMap)submittedAnswers)) {
            userSessionData.setValue("account-verified-id", userId);
            return trackProgress(success()
            .feedback("verify-account.success")
            .build());
        } else {
            return trackProgress(failed()
            .feedback("verify-account.failed")
            .build());
        }

    }

    private HashMap<String,String> parseSecQuestions (HttpServletRequest req) {

        Map <String,String> userAnswers = new HashMap<>();
        List<String> paramNames = Collections.list(req.getParameterNames());
        for  (String paramName : paramNames) {
            //String paramName = req.getParameterNames().nextElement();
            if (paramName.contains("secQuestion")) {
                userAnswers.put(paramName,req.getParameter(paramName));
            }
        }
        return (HashMap)userAnswers;

    }

}
