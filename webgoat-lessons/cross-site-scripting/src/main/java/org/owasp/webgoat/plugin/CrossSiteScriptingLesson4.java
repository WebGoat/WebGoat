package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AssignmentPath("CrossSiteScripting/attack4")
@AssignmentHints(value = {"xss-mitigation-4-hint1"})
public class CrossSiteScriptingLesson4 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String editor2) {

        String editor = editor2.replaceAll("\\<.*?>","");
        System.out.println(editor);

        if ((editor.contains("Policy.getInstance(\"antisamy-slashdot.xml\"") || editor.contains(".scan(newComment, \"antisamy-slashdot.xml\"") || editor.contains(".scan(newComment, new File(\"antisamy-slashdot.xml\")")) &&
                editor.contains("new AntiSamy();")&&
                editor.contains(".scan(newComment,") &&
                editor.contains("CleanResults") &&
                editor.contains("MyCommentDAO.addComment(threadID, userID")&&
                editor.contains(".getCleanHTML());"))
        {
            System.out.println("true");
            return trackProgress(success().feedback("xss-mitigation-4-success").build());
        }
        else {
            System.out.println("false");
            return trackProgress(failed().feedback("xss-mitigation-4-failed").build());
        }
    }
}
