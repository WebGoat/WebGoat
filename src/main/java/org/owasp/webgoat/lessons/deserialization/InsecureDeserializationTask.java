package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.lessons.deserialization.SerializationHelper.isAdmin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3",
  "insecure-deserialization.hints.4"
})
public class InsecureDeserializationTask extends AssignmentEndpoint {

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final String SECRET_KEY = "WebGoat_Secret_Key_Do_Not_Use_In_Production";
  private static final String SEPARATOR = ":";

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult execute(@RequestParam String token) throws IOException {
    if (StringUtils.isEmpty(token)) {
      return failed(this).feedback("insecure-deserialization.token.empty").build();
    }

    try {
      String[] parts = token.split(SEPARATOR);
      if (parts.length != 2) {
        return failed(this).feedback("insecure-deserialization.token.invalid-format").build();
      }

      String data = parts[0];
      String providedHmac = parts[1];

      // Verify HMAC
      if (!verifyHmac(data, providedHmac)) {
        return failed(this).feedback("insecure-deserialization.token.hmac-invalid").build();
      }

      // Deserialize with validation
      byte[] serializedObject = Base64.getDecoder().decode(data);
      Object object = safeDeserialize(serializedObject);

      if (object instanceof SerializationHelper.VulnerableTaskHolder) {
        SerializationHelper.VulnerableTaskHolder vtk = (SerializationHelper.VulnerableTaskHolder) object;
        if (isAdmin(vtk.getTaskName())) {
          return success(this).build();
        } else {
          return failed(this).feedback("insecure-deserialization.task.no-admin").build();
        }
      } else {
        return failed(this).feedback("insecure-deserialization.task.wrong-object").build();
      }
    } catch (IllegalArgumentException e) {
      return failed(this).feedback("insecure-deserialization.token.invalid-encoding").build();
    } catch (IOException | ClassNotFoundException e) {
      return failed(this).feedback("insecure-deserialization.task.failure").output(e.getMessage()).build();
    }
  }

  private boolean verifyHmac(String data, String provide
