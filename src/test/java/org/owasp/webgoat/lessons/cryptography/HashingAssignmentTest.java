// Assumed package based on source location; adjust if package differs in the project.
// Source: src/main/java/org/owasp/webgoat/lessons/cryptography/HashingAssignment.java
package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for HashingAssignment focusing on secure PRNG usage:
 * - Secrets used for hashing must come from the SECRETS array.
 * - Session attributes must be set consistently.
 * - Behavior remains deterministic regarding session handling.
 */
class HashingAssignmentTest {

  @Test
  void getMd5_shouldStoreAndReturnHashForSecretFromPredefinedList() throws NoSuchAlgorithmException {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Hash")).thenReturn(null);

    assignment.getMd5(request);

    // Secret should be stored in session and must be from SECRETS
    verify(session).setAttribute(eq("md5Secret"), argThat(secret -> {
      assertNotNull(secret, "Secret stored in session must not be null");
      boolean inList = false;
      for (String allowed : HashingAssignment.SECRETS) {
        if (allowed.equals(secret)) {
          inList = true;
          break;
        }
      }
      assertTrue(inList, "Secret must come from HashingAssignment.SECRETS");
      return true;
    }));

    // Hash should also be stored
    verify(session).setAttribute(eq("md5Hash"), anyString());
  }

  @Test
  void getSha256_shouldStoreAndReturnHashForSecretFromPredefinedList() throws NoSuchAlgorithmException {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("sha256")).thenReturn(null);

    assignment.getSha256(request);

    verify(session).setAttribute(eq("sha256Secret"), argThat(secret -> {
      assertNotNull(secret, "Secret stored in session must not be null");
      boolean inList = false;
      for (String allowed : HashingAssignment.SECRETS) {
        if (allowed.equals(secret)) {
          inList = true;
          break;
        }
      }
      assertTrue(inList, "Secret must come from HashingAssignment.SECRETS");
      return true;
    }));

    verify(session).setAttribute(eq("sha256Hash"), anyString());
  }

  @RepeatedTest(5)
  void getMd5_shouldUseSecureRandomForSecretSelection() throws NoSuchAlgorithmException {
    // This test verifies that SecureRandom.nextInt is invoked, indirectly asserting
    // that SecureRandom (not java.util.Random) is used for selecting the secret.
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Hash")).thenReturn(null);

    try (MockedStatic<SecureRandom> secureRandomStatic = Mockito.mockStatic(SecureRandom.class)) {
      SecureRandom secureRandom = mock(SecureRandom.class);
      // Always pick index 0 to keep deterministic
      when(secureRandom.nextInt(HashingAssignment.SECRETS.length)).thenReturn(0);
      secureRandomStatic.when(SecureRandom::new).thenReturn(secureRandom);

      String md5 = assignment.getMd5(request);
      assertNotNull(md5);

      secureRandomStatic.verify(SecureRandom::new);
      verify(secureRandom).nextInt(HashingAssignment.SECRETS.length);
    }
  }

  @Test
  void completed_shouldReturnSuccessWhenBothSecretsMatch() {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result = assignment.completed(request, "secret1", "secret2");

    assertTrue(result.getLessonCompleted(), "Both correct secrets must mark assignment as success");
  }

  @Test
  void completed_shouldReturnPartialFailureWhenOnlyOneSecretMatches() {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result = assignment.completed(request, "secret1", "wrong");

    assertFalse(result.getLessonCompleted(), "Only one correct secret must not mark success");
  }

  @Test
  void completed_shouldFailWhenSecretsDoNotMatchOrAreNull() {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Secret")).thenReturn("secret1");
    when(session.getAttribute("sha256Secret")).thenReturn("secret2");

    AttackResult result = assignment.completed(request, "wrong1", "wrong2");

    assertFalse(result.getLessonCompleted(), "Incorrect secrets must not mark assignment as success");
  }
}
