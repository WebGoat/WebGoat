package org.owasp.webgoat.lessons.vulnerablecomponents;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermitted;
import com.thoughtworks.xstream.security.NoTypePermitted;
import com.thoughtworks.xstream.security.PrimitiveTypePermitted;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"vulnerable.hint"})
public class VulnerableComponentsLesson implements AssignmentEndpoint {

  @PostMapping("/VulnerableComponents/attack1")
  public @ResponseBody AttackResult completed(@RequestParam String payload) {
    XStream xstream = new XStream();
    xstream.setClassLoader(Contact.class.getClassLoader());
    
    // CRITICAL: Apply security restrictions
    // 1. Start with denying all types
    xstream.addPermission(NoTypePermitted.NONE);
    
    // 2. Explicitly allow only safe classes
    xstream.addPermission(PrimitiveTypePermitted.PRIMITIVES);
    xstream.allowTypes(new Class[]{ContactImpl.class, Contact.class, String.class});
    
    xstream.alias("contact", ContactImpl.class);
    xstream.ignoreUnknownElements();
    
    Contact contact = null;

    try {
      if (!StringUtils.isEmpty(payload)) {
        // Validation: reject suspicious patterns
        if (payload.contains("java.") || payload.contains("Runtime") || 
            payload.contains("ProcessBuilder") || payload.contains("exec")) {
          return failed(this).feedback("vulnerable-components.close")
              .output("Suspicious payload detected").build();
        }
        
        // Normalize safely
        payload = payload
            .replace("+", "")
            .replace("\r", "")
            .replace("\n", "")
            .replace("> ", ">")
            .replace(" <", "<");
      }
      contact = (Contact) xstream.fromXML(payload);
    } catch (Exception ex) {
      return failed(this).feedback("vulnerable-components.close")
          .output(ex.getMessage()).build();
    }

    try {
      if (null != contact) {
        contact.getFirstName();
      }
      if (!(contact instanceof ContactImpl)) {
        return success(this).feedback("vulnerable-components.success").build();
      }
    } catch (Exception e) {
      return success(this).feedback("vulnerable-components.success")
          .output(e.getMessage()).build();
    }
    return failed(this).feedback("vulnerable-components.fromXML")
        .feedbackArgs(contact).build();
  }
}
