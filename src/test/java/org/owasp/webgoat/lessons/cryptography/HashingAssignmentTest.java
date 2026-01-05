// Assumed package based on source file location; adjust if actual package differs.
package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Delta tests for HashingAssignment focusing on the change from Random to SecureRandom when
 * selecting a secret. The intent is to guard against regressions that reintroduce predictable PRNG
 * usage.
 */
public class HashingAssignmentTest {

  private HashingAssignment hashingAssignment;
  private HttpServletRequest request;
  private HttpSession session;

  @BeforeEach
  void setUp() {
    hashingAssignment = new HashingAssignment();
    request = Mockito.mock(HttpServletRequest.class);
    session = Mockito.mock(HttpSession.class);
    Mockito.when(request.getSession()).thenReturn(session);
  }

  @Test
  void getMd5_shouldGenerateNewHashWhenNotInSession() throws NoSuchAlgorithmException {
    // Arrange
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

    // Act
    String hash = hashingAssignment.getMd5(request);

    // Assert
    Mockito.verify(session).setAttribute(Mockito.eq("md5Hash"), Mockito.eq(hash));
    Mockito.verify(session).setAttribute(Mockito.eq("md5Secret"), Mockito.anyString());
  }

  @Test
  void getMd5_shouldReuseHashFromSessionIfPresent() throws NoSuchAlgorithmException {
    // Arrange
    String existingHash = "EXISTING_HASH";
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(existingHash);

    // Act
    String result = hashingAssignment.getMd5(request);

    // Assert
    assertEquals(existingHash, result);
    Mockito.verify(session, Mockito.never())
        .setAttribute(Mockito.eq("md5Hash"), Mockito.anyString());
  }

  @Test
  void getSha256_shouldGenerateNewHashWhenNotInSession() throws NoSuchAlgorithmException {
    // Arrange
    Mockito.when(session.getAttribute("sha256")).thenReturn(null);

    // Act
    String hash = hashingAssignment.getSha256(request);

    // Assert
    Mockito.verify(session).setAttribute(Mockito.eq("sha256Hash"), Mockito.eq(hash));
    Mockito.verify(session).setAttribute(Mockito.eq("sha256Secret"), Mockito.anyString());
  }

  @Test
  void getSha256_shouldReuseHashFromSessionIfPresent() throws NoSuchAlgorithmException {
    // Arrange
    String existingHash = "EXISTING_SHA256_HASH";
    Mockito.when(session.getAttribute("sha256")).thenReturn(existingHash);

    // Act
    String result = hashingAssignment.getSha256(request);

    // Assert
    assertEquals(existingHash, result);
    Mockito.verify(session, Mockito.never())
        .setAttribute(Mockito.eq("sha256Hash"), Mockito.anyString());
  }

  @Test
  void getMd5_multipleCallsShouldNotAlwaysReturnSameHash_whenNoSessionValue()
      throws NoSuchAlgorithmException {
    // This test is probabilistic but aimed to catch an obvious regression to a fixed or predictable
    // value (e.g., hard-coded secret). It does not prove full cryptographic strength, only that
    // two independent calls can yield different values.
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(null, null);

    String first = hashingAssignment.getMd5(request);
    String second = hashingAssignment.getMd5(request);

    // If SecureRandom is replaced by a fixed value or constant secret, these will likely match.
    assertNotEquals(first, second);
  }
}
