package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

/**
 * Delta tests focusing on the change from java.util.Random to SecureRandom
 * for secret selection in HashingAssignment.
 */
class HashingAssignmentTest {

  @Test
  void getMd5_usesSecureRandomForSecretSelection() throws NoSuchAlgorithmException {
    HashingAssignment hashingAssignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Hash")).thenReturn(null);

    // Control SecureRandom output deterministically
    try (MockedStatic<SecureRandom> secureRandomStatic = mockStatic(SecureRandom.class)) {
      SecureRandom mockSecureRandom = mock(SecureRandom.class);
      when(SecureRandom.getInstanceStrong()).thenReturn(mockSecureRandom);
      when(mockSecureRandom.nextInt(HashingAssignment.SECRETS.length)).thenReturn(2); // pick "password"

      String result = hashingAssignment.getMd5(request);

      assertNotNull(result); // ensures flow executed
      // Verify SecureRandom was used exactly once and Random was not involved
      secureRandomStatic.verify(SecureRandom::getInstanceStrong, times(1));
      verify(mockSecureRandom, times(1)).nextInt(HashingAssignment.SECRETS.length);
      verify(session).setAttribute(eq("md5Secret"), eq(HashingAssignment.SECRETS[2]));
    }
  }

  @Test
  void getSha256_usesSecureRandomForSecretSelection() throws NoSuchAlgorithmException {
    HashingAssignment hashingAssignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("sha256")).thenReturn(null);

    try (MockedStatic<SecureRandom> secureRandomStatic = mockStatic(SecureRandom.class)) {
      SecureRandom mockSecureRandom = mock(SecureRandom.class);
      when(SecureRandom.getInstanceStrong()).thenReturn(mockSecureRandom);
      when(mockSecureRandom.nextInt(HashingAssignment.SECRETS.length)).thenReturn(1); // pick "admin"

      String result = hashingAssignment.getSha256(request);

      assertNotNull(result);
      secureRandomStatic.verify(SecureRandom::getInstanceStrong, times(1));
      verify(mockSecureRandom, times(1)).nextInt(HashingAssignment.SECRETS.length);
      verify(session).setAttribute(eq("sha256Secret"), eq(HashingAssignment.SECRETS[1]));
    }
  }
}
