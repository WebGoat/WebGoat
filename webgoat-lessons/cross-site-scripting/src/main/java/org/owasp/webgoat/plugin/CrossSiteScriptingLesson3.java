package org.owasp.webgoat.plugin.mitigation;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

        Document doc = Jsoup.parse(editor);
        String[] lines = editor.split(System.getProperty("line.separator"));

        String include = (lines[0]);
        String first_name_element = doc.select("body > table > tbody > tr:nth-child(1) > td:nth-child(2)").first().text();
        String last_name_element = doc.select("body > table > tbody > tr:nth-child(2) > td:nth-child(2)").first().text();

        Boolean includeCorrect = false;
        Boolean firstNameCorrect = false;
        Boolean lastNameCorrect = false;
        if(include.contains("<%@") && include.contains("taglib") && include.contains("uri=\"https://www.owasp.org/index.php/OWASP_Java_Encoder_Project\"") && include.contains("%>")){
            includeCorrect = true;
        }
        if(first_name_element.equals("${e:forHtml(param.first_name)}")){
            firstNameCorrect = true;
        }
        if(last_name_element.equals("${e:forHtml(param.last_name)}")){
            lastNameCorrect = true;
        }

        if(includeCorrect && firstNameCorrect && lastNameCorrect){
            System.out.println("true");
            return trackProgress(success().feedback("xss-mitigation-3-success").build());
        } else {
            System.out.println("false");
            System.out.println(first_name_element + "\n" + last_name_element);
            return trackProgress(failed().feedback("xss-mitigation-3-failure").build());
        }
    }
}
