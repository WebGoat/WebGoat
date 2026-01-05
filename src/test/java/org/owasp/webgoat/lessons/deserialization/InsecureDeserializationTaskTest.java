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

  private static String serializeToWebGoatToken(Object o) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(o);
    }
    String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
    return base64.replace('+', '-').replace('/', '_');
  }

  @Test
  @DisplayName("completed: should accept VulnerableTaskHolder (whitelisted type) when delay is in expected range")
  void completed_allowsWhitelistedType() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();
    VulnerableTaskHolder holder = new VulnerableTaskHolder();
    String token = serializeToWebGoatToken(holder);
    AttackResult result = task.completed(token);
    assertFalse(
        result.getFeedback().orElse("").contains("insecure-deserialization.stringobject"));
    assertFalse(
        result.getFeedback().orElse("").contains("insecure-deserialization.wrongobject"));
  }

  @Test
  @DisplayName("completed: should reject non-whitelisted type (e.g., Integer) via filter")
  void completed_rejectsNonWhitelistedType() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();
    String token = serializeToWebGoatToken(Integer.valueOf(42));
    AttackResult result = task.completed(token);
    assertFalse(result.getLessonCompleted());
  }
}
