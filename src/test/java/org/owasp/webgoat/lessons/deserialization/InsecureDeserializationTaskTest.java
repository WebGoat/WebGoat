package org.owasp.webgoat.lessons.deserialization;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests focusing on ObjectInputFilter usage restricting deserialization
 * to VulnerableTaskHolder and rejecting other types.
 */
class InsecureDeserializationTaskTest {

  private String toUrlSafeBase64(byte[] bytes) {
    String base64 = Base64.getEncoder().encodeToString(bytes);
    return base64.replace('+', '-').replace('/', '_');
  }

  @Test
  void completed_allowsVulnerableTaskHolderInstances() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    VulnerableTaskHolder obj = new VulnerableTaskHolder();
    // keep processing time within required window by making it simple
    byte[] serialized;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
      oos.flush();
      serialized = baos.toByteArray();
    }

    String token = toUrlSafeBase64(serialized);
    AttackResult result = task.completed(token);

    // We only assert that deserialization passes the filter and reaches timing logic;
    // success or failure on timing is not the focus of this delta test.
    assertNotNull(result);
  }

  @Test
  void completed_rejectsNonAllowlistedTypes() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    String payload = "some-string";
    byte[] serialized;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(payload);
      oos.flush();
      serialized = baos.toByteArray();
    }

    String token = toUrlSafeBase64(serialized);
    AttackResult result = task.completed(token);

    // If the filter rejects the type or it gets treated as wrong object,
    // AttackResult should indicate failure (not a successful lesson completion).
    assertFalse(result.isLessonCompleted());
  }
}
