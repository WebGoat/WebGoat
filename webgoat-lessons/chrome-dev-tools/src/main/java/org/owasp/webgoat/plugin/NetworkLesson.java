package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Assignment where the user has to look through an HTTP Request
 * using the Developer Tools and find a specific number.
 * @author TMelzer
 * @since 30.11.18
 */
@AssignmentPath("/ChromeDevTools/network")
@AssignmentHints({"networkHint1", "networkHint2"})
public class NetworkLesson extends AssignmentEndpoint {

  @RequestMapping(method = RequestMethod.POST, params= {"network_num","number"})
  public
  @ResponseBody
  AttackResult completed(@RequestParam String network_num, @RequestParam String number) throws IOException {
    if(network_num.equals(number)) {
      return trackProgress(success().feedback("network.success").output("").build());
    } else {
      return trackProgress(failed().feedback("network.failed").build());
    }
  }
  
  @RequestMapping(method = RequestMethod.POST, params="networkNum")
  public
  @ResponseBody
  ResponseEntity<?> ok(@RequestParam String networkNum) throws IOException {
	  return ResponseEntity.ok().build();
  }
}
