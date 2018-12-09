package org.owasp.webgoat.plugin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@AssignmentPath("CrossSiteScripting/attack3")
@AssignmentHints(value = {"xss-mitigation-3-hint1", "xss-mitigation-3-hint2", "xss-mitigation-3-hint3", "xss-mitigation-3-hint4"})
public class CrossSiteScriptingLesson3 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String editor) {
        String unescapedString = org.jsoup.parser.Parser.unescapeEntities(editor, true);
        try {
            if (editor.isEmpty()) return trackProgress(failed().feedback("xss-mitigation-3-no-code").build());
            Document doc = Jsoup.parse(unescapedString);
            String[] lines = unescapedString.split("<html>");

            String include = (lines[0]);
            String first_name_element = doc.select("body > table > tbody > tr:nth-child(1) > td:nth-child(2)").first().text();
            String last_name_element = doc.select("body > table > tbody > tr:nth-child(2) > td:nth-child(2)").first().text();

            Boolean includeCorrect = false;
            Boolean firstNameCorrect = false;
            Boolean lastNameCorrect = false;

            if (include.contains("<%@") && include.contains("taglib") && include.contains("uri=\"https://www.owasp.org/index.php/OWASP_Java_Encoder_Project\"") && include.contains("%>")) {
                includeCorrect = true;
            }
            if (first_name_element.equals("${e:forHtml(param.first_name)}")) {
                firstNameCorrect = true;
            }
            if (last_name_element.equals("${e:forHtml(param.last_name)}")) {
                lastNameCorrect = true;
            }

            if (includeCorrect && firstNameCorrect && lastNameCorrect) {
                System.out.println("true");
                return trackProgress(success().feedback("xss-mitigation-3-success").build());
            } else {
                return trackProgress(failed().feedback("xss-mitigation-3-failure").build());
            }
        }catch(Exception e) {
            return trackProgress(failed().output(e.getMessage()).build());
        }
    }
}
