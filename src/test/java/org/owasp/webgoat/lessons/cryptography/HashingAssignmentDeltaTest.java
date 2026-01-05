// NOTE: Package inferred from source file; adjust if necessary.
package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Delta unit tests for HashingAssignment focusing on the changed behavior:
 * - Use of SecureRandom instead of Random for selecting secrets.
 * - Ensuring secrets/derived hashes are stored in the session as expected.
 *
 * These tests do NOT assert the exact random distribution (which would be non-deterministic),
 * but instead focus on:
 *  - Values being taken from SECRETS.
 *  - Session attributes being properly set when not already present.
 *  - Endpoints returning non-null, non-empty responses.
 */
class HashingAssignmentDeltaTest {

  private HashingAssignment hashingAssignment;
  private HttpServletRequest request;
  private HttpSession session;

  @BeforeEach
  void setUp() {
    hashingAssignment = new HashingAssignment();
    request = mock(HttpServletRequest.class);
    session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
  }

  @Test
  void getMd5_whenNoExistingHash_shouldSelectSecretFromSecretsAndStoreInSession() throws NoSuchAlgorithmException {
    // Arrange
    when(session.getAttribute("md5Hash")).thenReturn(null);

    // Act
    String result = hashingAssignment.getMd5(request);

    // Assert
    assertNotNull(result, "MD5 hash should not be null");
    assertFalse(result.isEmpty(), "MD5 hash should not be empty");

    // Verify that session attributes were set
    ArgumentCaptor<String> attrNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Object> attrValueCaptor = ArgumentCaptor.forClass(Object.class);
    verify(session, atLeast(1)).setAttribute(attrNameCaptor.capture(), attrValueCaptor.capture());

    boolean md5HashSet = false;
    boolean md5SecretSet = false;
    String md5HashValue = null;
    String md5SecretValue = null;

    for (int i = 0; i < attrNameCaptor.getAllValues().size(); i++) {
      String name = attrNameCaptor.getAllValues().get(i);
      Object value = attrValueCaptor.getAllValues().get(i);
      if ("md5Hash".equals(name)) {
        md5HashSet = true;
        md5HashValue = (String) value;
      } else if ("md5Secret".equals(name)) {
        md5SecretSet = true;
        md5SecretValue = (String) value;
      }
    }

    assertTrue(md5HashSet, "Session attribute 'md5Hash' should be set");
    assertTrue(md5SecretSet, "Session attribute 'md5Secret' should be set");
    assertEquals(result, md5HashValue, "Returned hash should match 'md5Hash' session attribute");

    // Ensure selected secret is one of the defined SECRETS (indirectly validating selection from array)
    assertNotNull(md5SecretValue, "md5Secret should not be null");
    assertTrue(
        isValueInSecrets(md5SecretValue),
        "md5Secret should be selected from HashingAssignment.SECRETS"
    );
  }

  @Test
  void getMd5_whenExistingHash_shouldReturnExistingHashWithoutModifyingSecret() throws NoSuchAlgorithmException {
    // Arrange
    String existingHash = "EXISTING_HASH";
    String existingSecret = "secret";
    when(session.getAttribute("md5Hash")).thenReturn(existingHash);
    when(session.getAttribute("md5Secret")).thenReturn(existingSecret);

    // Act
    String result = hashingAssignment.getMd5(request);

    // Assert
    assertEquals(existingHash, result, "Should return existing md5Hash from session");
    // No new attributes should be set since hash already exists
    verify(session, never()).setAttribute(eq("md5Hash"), any());
    verify(session, never()).setAttribute(eq("md5Secret"), any());
  }

  @Test
  void getSha256_whenNoExistingHash_shouldSelectSecretFromSecretsAndStoreInSession()
      throws NoSuchAlgorithmException {
    // Arrange
    when(session.getAttribute("sha256")).thenReturn(null);

    // Act
    String result = hashingAssignment.getSha256(request);

    // Assert
    assertNotNull(result, "SHA-256 hash should not be null");
    assertFalse(result.isEmpty(), "SHA-256 hash should not be empty");

    // Verify that session attributes were set
    ArgumentCaptor<String> attrNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Object> attrValueCaptor = ArgumentCaptor.forClass(Object.class);
    verify(session, atLeast(1)).setAttribute(attrNameCaptor.capture(), attrValueCaptor.capture());

    boolean sha256HashSet = false;
    boolean sha256SecretSet = false;
    String sha256HashValue = null;
    String sha256SecretValue = null;

    for (int i = 0; i < attrNameCaptor.getAllValues().size(); i++) {
      String name = attrNameCaptor.getAllValues().get(i);
      Object value = attrValueCaptor.getAllValues().get(i);
      if ("sha256Hash".equals(name)) {
        sha256HashSet = true;
        sha256HashValue = (String) value;
      } else if ("sha256Secret".equals(name)) {
        sha256SecretSet = true;
        sha256SecretValue = (String) value;
      }
    }

    assertTrue(sha256HashSet, "Session attribute 'sha256Hash' should be set");
    assertTrue(sha256SecretSet, "Session attribute 'sha256Secret' should be set");
    assertEquals(result, sha256HashValue, "Returned hash should match 'sha256Hash' session attribute");

    // Ensure selected secret is one of the defined SECRETS (indirectly validating selection from array)
    assertNotNull(sha256SecretValue, "sha256Secret should not be null");
    assertTrue(
        isValueInSecrets(sha256SecretValue),
        "sha256Secret should be selected from HashingAssignment.SECRETS"
    );
  }

  @Test
  void getSha256_whenExistingHash_shouldReturnExistingHashWithoutModifyingSecret()
      throws NoSuchAlgorithmException {
    // Arrange
    String existingHash = "EXISTING_SHA256_HASH";
    String existingSecret = "password";
    when(session.getAttribute("sha256")).thenReturn(existingHash);
    when(session.getAttribute("sha256Secret")).thenReturn(existingSecret);

    // Act
    String result = hashingAssignment.getSha256(request);

    // Assert
    assertEquals(existingHash, result, "Should return existing sha256 from session");
    // No new attributes should be set since hash already exists
    verify(session, never()).setAttribute(eq("sha256Hash"), any());
    verify(session, never()).setAttribute(eq("sha256Secret"), any());
  }

  @RepeatedTest(5)
  void getMd5_shouldNotThrowAndProduceValuesWithinExpectedDomain() throws NoSuchAlgorithmException {
    // This repeated test focuses on robustness and the fact that randomness
    // (now via SecureRandom) does not break the method contract.
    when(session.getAttribute("md5Hash")).thenReturn(null);

    String result = hashingAssignment.getMd5(request);

    assertNotNull(result, "MD5 hash should not be null");
    assertFalse(result.isEmpty(), "MD5 hash should not be empty");
  }

  @RepeatedTest(5)
  void getSha256_shouldNotThrowAndProduceValuesWithinExpectedDomain() throws NoSuchAlgorithmException {
    when(session.getAttribute("sha256")).thenReturn(null);

    String result = hashingAssignment.getSha256(request);

    assertNotNull(result, "SHA-256 hash should not be null");
    assertFalse(result.isEmpty(), "SHA-256 hash should not be empty");
  }

  // Helper to assert that a chosen secret belongs to the SECRETS array.
  private boolean isValueInSecrets(String value) {
    for (String s : HashingAssignment.SECRETS) {
      if (s.equals(value)) {
        return true;
      }
    }
    return false;
  }
}
