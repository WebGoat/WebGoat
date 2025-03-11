package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.lessons.deserialization.SerializationHelper.isAdmin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3",
  "insecure-deserialization.hints.4"
})
public class InsecureDeserializationTask extends AssignmentEndpoint {

  private String secretPassword;

  @PostConstruct
  public void setup() {
    secretPassword = "secret";
  }

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) {
    if (StringUtils.isEmpty(token)) {
      return failed(this).feedback("insecure-deserialization.token.empty").build();
    }
    try {
      byte[] base64Token = Base64.getDecoder().decode(token);
      
      // Create a safe deserializer with validation
      ByteArrayInputStream bis = new ByteArrayInputStream(base64Token);
      ValidatingObjectInputStream ois = new ValidatingObjectInputStream(bis);
      
      // Only allow deserialization of known safe classes
      ois.accept(SerializationHelper.VulnerableTaskHolder.class);
      
      Object o = ois.readObject();
      
      if (o instanceof SerializationHelper.VulnerableTaskHolder) {
        SerializationHelper.VulnerableTaskHolder taskHolder = (SerializationHelper.VulnerableTaskHolder) o;
        if (taskHolder != null
            && StringUtils.reverse(secretPassword).equals(taskHolder.getTaskDescription())) {
          return success(this).build();
        }
      }
      return failed(this).feedback("insecure-deserialization.token.invalid").build();
    } catch (IllegalArgumentException e) {
      return failed(this).feedback("insecure-deserialization.token.invalid").build();
    } catch (IOException | ClassNotFoundException e) {
      return failed(this).feedback("insecure-deserialization.token.invalid").build();
    }
  }
}
