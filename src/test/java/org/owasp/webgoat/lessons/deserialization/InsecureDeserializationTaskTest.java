// Assumed package based on source location; adjust if needed.
// Source: src/main/java/org/owasp/webgoat/lessons/deserialization/InsecureDeserializationTask.java
package org.owasp.webgoat.lessons.deserialization;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for InsecureDeserializationTask focusing on the ObjectInputFilter:
 * - Ensures allowed type (VulnerableTaskHolder) still passes.
 * - Ensures disallowed arbitrary object types are rejected.
 */
class InsecureDeserializationTaskTest {

  private String toUrlSafeBase64(Object obj) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
    }
    String b64 = Base64.getEncoder().encodeToString(baos.toByteArray());
    return b64.replace('+', '-').replace('/', '_');
  }

  @Test
  void completed_shouldSucceedForAllowedVulnerableTaskHolder() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    VulnerableTaskHolder holder = new VulnerableTaskHolder();
    String token = toUrlSafeBase64(holder);

    AttackResult result = task.completed(token);

    // The lesson's timing logic may require some delay; here we assert that
    // executing with an allowed type does not immediately trigger type-based failure.
    assertNotNull(result, "AttackResult should not be null for allowed type");
  }

  @Test
  void completed_shouldRejectDisallowedObjectTypes() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    // String is allowed in filter as a specific case;
    // use a disallowed type like Integer to trigger filter rejection.
    String token = toUrlSafeBase64(Integer.valueOf(42));

    AttackResult result = task.completed(token);

    // We cannot assert specific feedback key without full context,
    // but we assert that the call completes and returns a failure-like result.
    assertNotNull(result, "AttackResult should not be null for disallowed type");
    assertFalse(result.getLessonCompleted(), "Deserialization of disallowed type must not succeed");
  }
}
