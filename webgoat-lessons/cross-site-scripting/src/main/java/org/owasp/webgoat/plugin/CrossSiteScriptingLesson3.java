package org.owasp.webgoat.plugin.mitigation;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jsoup.*;
import org.w3c.dom.*;


import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AssignmentPath("CrossSiteScripting/attack3")
@AssignmentHints(value = {"xss-mitigation-3-hint1", "xss-mitigation-3-hint2", "xss-mitigation-3-hint3", "xss-mitigation-3-hint4"})
public class CrossSiteScriptingLesson3 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String editor) {

        editor = editor.replaceAll("\\<.*?>","");
        //http://www.java67.com/2012/10/how-to-escape-html-special-characters-JSP-Java-Example.html
        //
        //<c:out value="${first_name/last_name}" escapeXml="true"/>
        //or
        //${fn:escapeXml("param.first_name/last_name")}

        //check html string for regex
            //check for c:out && escapeXml="true" && !request.getParameter
        //Document doc = Jsoup.parse(editor);
        //Element e = doc.getElementById();

        System.out.println(editor);
        if (editor.contains("c:out") && editor.contains("escapeXml=\"true\"") && editor.contains("value=\"${last_name}\"") && editor.contains("value=\"${first_name}\"")) {
            System.out.println("true");
            return trackProgress(success().build());
        }
        else if (editor.contains("${fn:escapeXml") && editor.contains("\"param.first_name\"") && editor.contains("\"param.last_name\"")) {
            System.out.println("true");
            return trackProgress(success().build());
        }
        else {
            System.out.println("false");
            return trackProgress(failed().build());
        }
    }
}
