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
 * Delta tests for InsecureDeserializationTask focusing on the security-relevant change:
 * use of SecureObjectInputStream with an allow-list of VulnerableTaskHolder and String.
 *
 * These tests verify:
 *  1) A serialized VulnerableTaskHolder is still accepted and processed.
 *  2) A serialized String is still handled and produces the 'stringobject' feedback.
 *  3) A disallowed serialized type triggers the invalidversion-style failure.
 */
class InsecureDeserializationTaskTest {

  private final InsecureDeserializationTask insecureDeserializationTask =
      new InsecureDeserializationTask();

  /**
   * Helper to serialize an object and encode it as the token expected by the controller
   * (Base64 + URL-safe transformations).
   */
  private String toToken(Object obj) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
    }
    String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
    // The controller expects '-' for '+' and '_' for '/'
    return base64.replace('+', '-').replace('/', '_');
  }

  @Test
  @DisplayName("Serialized VulnerableTaskHolder is accepted (allowed by SecureObjectInputStream)")
  void testAllowedVulnerableTaskHolderStillAccepted() throws Exception {
    // Arrange
    // NOTE: We assume VulnerableTaskHolder is Serializable and present on the test classpath.
    VulnerableTaskHolder holder = new VulnerableTaskHolder();
    String token = toToken(holder);

    // Act
    AttackResult result = insecureDeserializationTask.completed(token);

    // Assert
    // We do not assert exact timing-based success criteria (which depend on the payload),
    // but we assert that the call does NOT immediately fail with invalidversion or stringobject,
    // which would indicate the allow-list blocked or misrouted the type.
    assertThat(result).isNotNull();
    String feedback = result.getFeedback();
    if (feedback != null) {
      assertThat(feedback)
          .as("VulnerableTaskHolder should not be treated as String or invalid version")
          .doesNotContain("insecure-deserialization.stringobject")
          .doesNotContain("insecure-deserialization.invalidversion");
    }
  }

  @Test
  @DisplayName("Serialized String is still handled and returns stringobject feedback")
  void testAllowedStringStillHandledWithStringobjectFeedback() throws Exception {
    // Arrange
    String payload = "just-a-string";
    String token = toToken(payload);

    // Act
    AttackResult result = insecureDeserializationTask.completed(token);

    // Assert
    assertThat(result).isNotNull();
    // For String payloads the code path returns feedback 'insecure-deserialization.stringobject'
    assertThat(result.getFeedback())
        .as("String payloads should trigger 'stringobject' feedback")
        .contains("insecure-deserialization.stringobject");
  }

  @Test
  @DisplayName("Disallowed type triggers invalidversion-style failure via InvalidClassException")
  void testDisallowedTypeTriggersInvalidVersionFeedback() throws Exception {
    // Arrange
    // A simple Serializable custom type that is NOT on the allow-list of SecureObjectInputStream
    class DisallowedType implements Serializable {
      private static final long serialVersionUID = 1L;
      String value = "disallowed";
    }

    String token = toToken(new DisallowedType());

    // Act
    AttackResult result = insecureDeserializationTask.completed(token);

    // Assert
    assertThat(result).isNotNull();
    // When SecureObjectInputStream rejects the class, it throws InvalidClassException
    // which is mapped to 'insecure-deserialization.invalidversion' feedback.
    assertThat(result.getFeedback())
        .as("Disallowed type should be rejected with 'invalidversion' feedback")
        .contains("insecure-deserialization.invalidversion");
  }
}
