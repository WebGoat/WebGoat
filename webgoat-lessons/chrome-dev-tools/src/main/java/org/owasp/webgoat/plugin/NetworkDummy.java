package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * This is just a class used to make the the HTTP request.
 * @author TMelzer
 * @since 30.11.18
 */
@AssignmentPath("/ChromeDevTools/dummy")
public class NetworkDummy extends AssignmentEndpoint {

  @RequestMapping(method = RequestMethod.POST)
  public
  @ResponseBody
  AttackResult completed(@RequestParam String successMessage) throws IOException {
	  
	  UserSessionData userSessionData = getUserSessionData();
      String answer = (String) userSessionData.getValue("randValue");

      if (successMessage!=null && successMessage.equals(answer)) {
          return trackProgress(success().feedback("xss-dom-message-success").build());
      } else {
          return trackProgress(failed().feedback("xss-dom-message-failure").build());
      }
	    
  }
}