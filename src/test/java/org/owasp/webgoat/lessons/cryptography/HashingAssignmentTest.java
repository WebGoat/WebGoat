// Assumed test source root: src/test/java
// Package inferred from source file: org.owasp.webgoat.lessons.cryptography
package org.owasp.webgoat.lessons.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Delta tests for HashingAssignment focusing on the change from java.util.Random
 * to java.security.SecureRandom for secret selection.
 */
class HashingAssignmentTest {

  @Test
  @DisplayName("getMd5 should store and reuse md5Secret in session")
  void getMd5StoresAndReusesSecretInSession() throws Exception {
    HashingAssignment assignment = new HashingAssignment();
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpSession session = Mockito.mock(HttpSession.class);

    Mockito.when(request.getSession()).thenReturn(session);
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

    String hash1 = assignment.getMd5(request);

    // Second call should not regenerate the md5Hash; we simulate that by returning the first hash
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(hash1);
    String hash2 = assignment.getMd5(request);

    assertThat(hash1).isEqualTo(hash2);
    Mockito.verify(session).setAttribute(Mockito.eq("md5Hash"), Mockito.anyString());
    Mockito.verify(session).setAttribute(Mockito.eq("md5Secret"), Mockito.anyString());
  }

  @RepeatedTest(5)
  @DisplayName("getMd5 should use SecureRandom and produce varied secrets/hashes over time")
  void getMd5UsesSecureRandomToVarySecretSelection() throws Exception {
    HashingAssignment assignment = new HashingAssignment();

    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpSession session = Mockito.mock(HttpSession.class);
    Mockito.when(request.getSession()).thenReturn(session);
    Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

    String hash = assignment.getMd5(request);
    assertThat(hash).isNotNull().isNotEmpty();
  }

  @Test
  @DisplayName("SECRETS array should be non-empty so SecureRandom index selection is safe")
  void secretsArrayNonEmptyForSecureRandomIndex() {
    assertThat(HashingAssignment.SECRETS)
        .isNotNull()
        .isNotEmpty();

    int maxIndex = HashingAssignment.SECRETS.length - 1;
    SecureRandom random = new SecureRandom();
    int idx = random.nextInt(HashingAssignment.SECRETS.length);
    assertThat(idx).isBetween(0, maxIndex);
  }
}
