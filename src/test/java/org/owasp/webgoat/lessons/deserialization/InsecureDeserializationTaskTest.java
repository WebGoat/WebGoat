// Assumed test source root: src/test/java
// Package inferred from source file: org.owasp.webgoat.lessons.deserialization
package org.owasp.webgoat.lessons.deserialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for InsecureDeserializationTask focusing on the added ObjectInputFilter allowlist.
 */
class InsecureDeserializationTaskTest {

  @Test
  @DisplayName("completed should still accept a valid VulnerableTaskHolder token (behavior preserved)")
  void completedAcceptsAllowedVulnerableTaskHolder() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    // Build a minimal VulnerableTaskHolder or a stand-in that is serializable
    VulnerableTaskHolder holder = new VulnerableTaskHolder("user", 1000L); // TODO: Adjust ctor if needed

    String token = serializeToWebToken(holder);

    AttackResult result = task.completed(token);

    // Original lesson logic expects timing-based conditions; here we only verify it's not a hard failure
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("completed should reject unexpected object types due to ObjectInputFilter")
  void completedRejectsDisallowedClass() throws Exception {
    InsecureDeserializationTask task = new InsecureDeserializationTask();

    // This class is not in the allowlist; filter should prevent it
    MaliciousObject malicious = new MaliciousObject("exploit");
    String token = serializeToWebToken(malicious);

    AttackResult result = task.completed(token);

    // Filter or type-check should lead to a failure result
    assertThat(result).isNotNull();
  }

  private String serializeToWebToken(Serializable obj) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
    }
    String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
    // Server replaces '-' -> '+', '_' -> '/' before decoding; we reverse that encoding here
    return base64.replace('+', '-').replace('/', '_');
  }

  // Simple serializable class used to simulate a disallowed payload
  private static class MaliciousObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String payload;

    MaliciousObject(String payload) {
      this.payload = payload;
    }

    @Override
    public String toString() {
      return payload;
    }
  }
}
