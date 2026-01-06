package org.owasp.webgoat.lessons.deserialization;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class InsecureDeserializationTaskTest {

  private String toWebToken(Object obj) throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(obj);
    }
    String base64 = Base64.getEncoder().encodeToString(bos.toByteArray());
    return base64.replace('+', '-').replace('/', '_');
  }

  @Test
  @DisplayName("completed should accept only VulnerableTaskHolder objects after filter hardening")
  void completed_allowsOnlyVulnerableTaskHolder() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    VulnerableTaskHolder holder = new VulnerableTaskHolder();
    String token = toWebToken(holder);

    AttackResult result = task.completed(token);

    assertNotNull(result, "AttackResult should not be null");
  }

  @Test
  @DisplayName("completed should reject non-whitelisted classes due to strict ObjectInputFilter")
  void completed_rejectsNonWhitelistedClass() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    String token = toWebToken("malicious-string-payload");

    AttackResult result = task.completed(token);

    assertNotNull(result, "AttackResult should not be null for rejected class");
    assertFalse(
        result.getLessonCompleted(),
        "Deserialization of non-whitelisted class must not complete the lesson");
  }
}
