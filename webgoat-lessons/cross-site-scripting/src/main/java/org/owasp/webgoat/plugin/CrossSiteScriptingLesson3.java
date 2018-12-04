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
@AssignmentHints(value = {"xss-mitigation-3-hint1", "xss-mitigation-3-hint2", "xss-mitigation-3-hint3", "xss-mitigation-3-hint4"})
public class CrossSiteScriptingLesson3 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String editor) {
        //https://github.com/OWASP/owasp-java-encoder
        //maybe better idea for assignment
        //<e:forHtml value="${param.title}" />

        String line1 ="";
        String line2 ="";

        String[] lines = editor.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].contains("First Name")){
                line1 = lines[i+1].replace("                <td>","").replace("</td>","");
            } else if (lines[i].contains("Last Name")){
                line2 = lines[i+1].replace("                <td>", "").replace("</td>", "");
            }
        }

        //<c:out value="${first_name/last_name}" escapeXml="true"/>
        //or
        //${fn:escapeXml("param.first_name/last_name")}

        if((line1.equals("<c:out value=\"${first_name}\" escapeXml=\"true\"/>") || line1.equals("<c:out escapeXml=\"true\" value=\"${first_name}\"/>"))
                && (line2.equals("<c:out value=\"${last_name}\" escapeXml=\"true\"/>")) || line2.equals("<c:out escapeXml=\"true\" value=\"${last_name}\" />")){
            System.out.println("true");
            return trackProgress(success().feedback("xss-mitigation-3-success").build());
        } else if(line1.equals("${fn:escapeXml(\"param.first_name\")}") && line2.equals("${fn:escapeXml(\"param.last_name\")}")){
            System.out.println("true");
            return trackProgress(success().feedback("xss-mitigation-3-success").build());
        } else {
            System.out.println("false");
            System.out.println(line1 + "\n" + line2);
            return trackProgress(failed().feedback("xss-mitigation-3-failure").build());
        }
    }
}
