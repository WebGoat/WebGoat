package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
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
  AttackResult completed(@RequestParam String networkNum) throws IOException {
    return trackProgress(failed().feedback("network.request").build());
  }
}