package org.owasp.webgoat.plugin.mitigation;

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

@AssignmentPath("CrossSiteScripting/attack3")
//@AssignmentHints(value = {"SqlStringInjectionHint-mitigation-10b-1", "SqlStringInjectionHint-mitigation-10b-2", "SqlStringInjectionHint-mitigation-10b-3"})
public class CrossSiteScriptingLesson3 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String editor) {

        String regex1 = "<(\\\"[^\\\"]*\\\"|'[^']*'|[^'\\\">])*>(.*<(\\\"[^\\\"]*\\\"|'[^']*'|[^'\\\">])*>)?"; //Insert regex to verify html
        editor = editor.replaceAll("\\<.*?>","");
        boolean hasImportant = this.check_text(regex1, editor.replace("\n", "").replace("\r", ""));

        //http://www.java67.com/2012/10/how-to-escape-html-special-characters-JSP-Java-Example.html
        //
        //<c:out value=${first_name/last_name} escapeXml='true'/>
        //or
        //${fn:escapeXml("param.first_name/last_name")}

        //check html string for regex
            //check for c:out && escapeXml="true" && !request.getParameter
        /**
        if(hasImportant && hasCompiled.size() < 1) {
            return trackProgress(success().build());
        } else if(hasCompiled.size() > 1) {
            for(Diagnostic d : hasCompiled) {
                errors += d.getMessage(null) + "\n";
            }
        }
         **/
        return trackProgress(failed().build());

    }

    private boolean check_text(String regex, String text) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if(m.find())
            return true;
        else return false;
    }
}
