package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashingAssignmentTest {

  @Test
  @DisplayName("getMd5: should be deterministic per session once secret is chosen")
  void getMd5_returnsSameHashForSameSession() throws NoSuchAlgorithmException {
    // Arrange
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    // First call: no hash in session
    when(session.getAttribute("md5Hash")).thenReturn(null);

    // We will capture the attributes set on session
    final String[] storedHash = new String[1];
    final String[] storedSecret = new String[1];
    doAnswer(
            inv -> {
              String name = inv.getArgument(0);
              Object value = inv.getArgument(1);
              if ("md5Hash".equals(name)) {
                storedHash[0] = (String) value;
              }
              if ("md5Secret".equals(name)) {
                storedSecret[0] = (String) value;
              }
              return null;
            })
        .when(session)
        .setAttribute(anyString(), any());

    // Act
    String first = assignment.getMd5(request);

    // Now simulate that the session already contains the stored hash
    when(session.getAttribute("md5Hash")).thenReturn(storedHash[0]);
    String second = assignment.getMd5(request);

    // Assert
    assertNotNull(storedSecret[0], "Secret should be stored in session");
    assertNotNull(first, "First MD5 hash should not be null");
    assertEquals(first, second, "MD5 hash should be stable for the same session and secret");
  }

  @Test
  @DisplayName("getSha256: multiple calls across fresh sessions should not all return the same value")
  void getSha256_usesNonTrivialRandomnessForSecrets() throws NoSuchAlgorithmException {
    // This test does a statistical-style sanity check that secrets are not chosen
    // in a trivially predictable way (e.g., constant or always first element).
    HashingAssignment assignment = new HashingAssignment();
    Set<String> hashes = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpSession session = mock(HttpSession.class);
      when(request.getSession()).thenReturn(session);
      when(session.getAttribute("sha256")).thenReturn(null);

      final String[] storedHash = new String[1];
      doAnswer(
              inv -> {
                if ("sha256Hash".equals(inv.getArgument(0))) {
                  storedHash[0] = (String) inv.getArgument(1);
                }
                return null;
              })
          .when(session)
          .setAttribute(anyString(), any());

      String hash = assignment.getSha256(request);
      assertEquals(
          storedHash[0], hash, "Method return should match session-stored SHA-256 hash");
      hashes.add(hash);
    }

    // We expect at least some variation in secrets / hashes when using SecureRandom.
    assertTrue(
        hashes.size() > 1,
        "SHA-256 hashes across different sessions should not all be identical, "
            + "indicating use of a non-trivial random source for secrets");
  }
}
