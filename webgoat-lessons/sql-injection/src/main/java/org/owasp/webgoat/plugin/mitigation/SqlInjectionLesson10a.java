package org.owasp.webgoat.plugin.mitigation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@AssignmentPath("SqlInjection/attack10a")
@Slf4j
@AssignmentHints(value = {"SqlStringInjectionHint-mitigation-10a-1", "SqlStringInjectionHint-mitigation-10a-10a2"})
public class SqlInjectionLesson10a extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;
    // @TODO: Maybe provide regex instead of "hard coded" strings
    private String[] results = {"getConnection", "PreparedStatement", "prepareStatement", "?", "?", "setString", "setString"};

    // @TODO Method head too big, better solution?
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SneakyThrows
    public AttackResult completed(@RequestParam String field1, @RequestParam String field2, @RequestParam String field3, @RequestParam String field4, @RequestParam String field5, @RequestParam String field6, @RequestParam String field7) {
        String[] userInput = {field1, field2, field3, field4, field5, field6, field7};
        int position = 0;
        boolean completed = false;
        for(String input : userInput) {
            if(input.toLowerCase().contains(this.results[position].toLowerCase())) {
                completed = true;
            } else {
                return trackProgress(failed().build());
            }
            position++;
        }
        if(completed) {
            return trackProgress(success().build());
        }
        return trackProgress(failed().build());
    }
}
