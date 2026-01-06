package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashingAssignmentTest {

  @Test
  @DisplayName("getMd5 should generate hash and store secret in session only once")
  void getMd5_usesSessionStoredSecret() throws NoSuchAlgorithmException {
    HashingAssignment assignment = new HashingAssignment();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Hash")).thenReturn(null);

    String md5HashFirst = assignment.getMd5(request);

    assertNotNull(md5HashFirst, "MD5 hash should be generated on first call");

    verify(session).setAttribute(eq("md5Hash"), eq(md5HashFirst));
    verify(session).setAttribute(eq("md5Secret"), anyString());

    reset(session);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("md5Hash")).thenReturn(md5HashFirst);

    String md5HashSecond = assignment.getMd5(request);

    assertEquals(
        md5HashFirst,
        md5HashSecond,
        "When a hash is already present in the session, it must be reused without new random selection");

    verify(session, never()).setAttribute(eq("md5Secret"), any());
    verify(session, never()).setAttribute(eq("md5Hash"), any());
  }

  @Test
  @DisplayName("getSha256 should generate hash and store secret in session only once")
  void getSha256_usesSessionStoredSecret() throws NoSuchAlgorithmException {
    HashingAssignment assignment = new HashingAssignment();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("sha256")).thenReturn(null);

    String shaFirst = assignment.getSha256(request);

    assertNotNull(shaFirst, "SHA-256 hash should be generated on first call");

    verify(session).setAttribute(eq("sha256Hash"), eq(shaFirst));
    verify(session).setAttribute(eq("sha256Secret"), anyString());

    reset(session);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute("sha256")).thenReturn(shaFirst);

    String shaSecond = assignment.getSha256(request);

    assertEquals(
        shaFirst,
        shaSecond,
        "When a SHA-256 hash is already present in the session, it must be reused without new random selection");

    verify(session, never()).setAttribute(eq("sha256Secret"), any());
    verify(session, never()).setAttribute(eq("sha256Hash"), any());
  }
}
