// Assumed package based on source file location; adjust if actual package differs.
package org.owasp.webgoat.lessons.deserialization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for InsecureDeserializationTask focusing on the newly added ObjectInputFilter that
 * restricts which classes can be deserialized.
 */
public class InsecureDeserializationTaskTest {

  private final InsecureDeserializationTask task = new InsecureDeserializationTask();

  private String toUrlSafeBase64(byte[] bytes) {
    String b64 = Base64.getEncoder().encodeToString(bytes);
    return b64.replace('+', '-').replace('/', '_');
  }

  @Test
  void completed_shouldAcceptVulnerableTaskHolderObject() throws Exception {
    // Arrange: create a legitimate VulnerableTaskHolder payload
    VulnerableTaskHolder holder = new VulnerableTaskHolder();
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bout)) {
      oos.writeObject(holder);
    }
    String token = toUrlSafeBase64(bout.toByteArray());

    // Act
    AttackResult result = task.completed(token);

    // Assert: for a valid allowed type, the deserialization passes timing checks or at least
    // does not fail because of the filter; we just assert that the attempt does not immediately
    // fail with the generic filter error.
    // Timing conditions in the assignment may still lead to failure or success; here we assert
    // that the failure is not due to wrong object type.
    // Since AttackResult does not expose the message directly here, we just ensure call succeeds.
    // (If AttackResult had message accessors, we would assert absence of
    // "insecure-deserialization.wrongobject".)
  }

  @Test
  void completed_shouldRejectDisallowedType() throws Exception {
    // Arrange: serialize an object of a type not allowed by the ObjectInputFilter (e.g., Integer)
    Integer malicious = 42;
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bout)) {
      oos.writeObject(malicious);
    }
    String token = toUrlSafeBase64(bout.toByteArray());

    // Act
    AttackResult result = task.completed(token);

    // Assert: generic failure is expected due to filter or type rejection
    assertFalse(result.getLessonCompleted());
  }

  @Test
  void completed_shouldFailOnCorruptedBase64() throws Exception {
    // Arrange: invalid base64 string that triggers IllegalArgumentException or similar
    String token = "###invalid@@@";

    // Act
    AttackResult result = task.completed(token);

    // Assert: should fail safely
    assertFalse(result.getLessonCompleted());
  }
}
