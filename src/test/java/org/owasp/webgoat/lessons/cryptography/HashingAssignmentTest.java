// Assumed package based on source file path; adjust if actual package differs.
package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

/**
 * Delta unit tests for HashingAssignment focusing on behavior affected by the fix:
 * - getMd5: initializes and reuses md5Hash/md5Secret in session.
 * - getSha256: initializes and reuses sha256Hash/sha256Secret in session.
 *
 * NOTE: We do not try to assert actual randomness (that would be non-deterministic),
 * only that session mapping and hash consistency behavior remain correct.
 */
public class HashingAssignmentTest {

    @Test
    @DisplayName("getMd5 should create and store md5Hash and md5Secret in session on first call")
    void getMd5_initializesSessionAttributesOnFirstCall() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        // First call: no attributes set
        Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

        // Act
        String result = assignment.getMd5(request);

        // Assert
        assertNotNull(result, "getMd5 should return a non-null hash value");
        // Verify that the method stores both hash and secret in the session
        Mockito.verify(session).setAttribute(Mockito.eq("md5Hash"), Mockito.eq(result));
        Mockito.verify(session).setAttribute(Mockito.eq("md5Secret"), Mockito.anyString());
    }

    @Test
    @DisplayName("getMd5 should reuse existing md5Hash from session on subsequent calls")
    void getMd5_reusesExistingSessionHashOnSubsequentCalls() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        String existingHash = "EXISTING_MD5_HASH";
        Mockito.when(request.getSession()).thenReturn(session);
        // Simulate that the hash is already present in session
        Mockito.when(session.getAttribute("md5Hash")).thenReturn(existingHash);

        // Act
        String result = assignment.getMd5(request);

        // Assert
        assertEquals(existingHash, result, "getMd5 should return the existing md5Hash from session");
        // When hash is already present, no new value should be stored
        Mockito.verify(session, Mockito.never()).setAttribute(Mockito.eq("md5Hash"), Mockito.any());
        Mockito.verify(session, Mockito.never()).setAttribute(Mockito.eq("md5Secret"), Mockito.any());
    }

    @Test
    @DisplayName("getSha256 should create and store sha256Hash and sha256Secret in session on first call")
    void getSha256_initializesSessionAttributesOnFirstCall() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        // First call: no attributes set
        Mockito.when(session.getAttribute("sha256")).thenReturn(null);

        // Act
        String result = assignment.getSha256(request);

        // Assert
        assertNotNull(result, "getSha256 should return a non-null hash value");
        Mockito.verify(session).setAttribute(Mockito.eq("sha256Hash"), Mockito.eq(result));
        Mockito.verify(session).setAttribute(Mockito.eq("sha256Secret"), Mockito.anyString());
    }

    @Test
    @DisplayName("getSha256 should reuse existing sha256 from session on subsequent calls")
    void getSha256_reusesExistingSessionHashOnSubsequentCalls() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        String existingSha = "EXISTING_SHA256_HASH";
        Mockito.when(request.getSession()).thenReturn(session);
        // Simulate that the hash is already present in session
        Mockito.when(session.getAttribute("sha256")).thenReturn(existingSha);

        // Act
        String result = assignment.getSha256(request);

        // Assert
        assertEquals(existingSha, result, "getSha256 should return the existing sha256 value from session");
        Mockito.verify(session, Mockito.never()).setAttribute(Mockito.eq("sha256Hash"), Mockito.any());
        Mockito.verify(session, Mockito.never()).setAttribute(Mockito.eq("sha256Secret"), Mockito.any());
    }

    @RepeatedTest(3)
    @DisplayName("getMd5 should consistently map session-stored secret to returned hash")
    void getMd5_secretAndHashConsistentAcrossCalls() throws NoSuchAlgorithmException {
        // This test validates that the mapping between secret and hash remains consistent
        // even after the switch to SecureRandom. It does NOT rely on randomness.
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

        // Capture the values stored to session
        Mockito.doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1);
            if ("md5Hash".equals(key)) {
                Mockito.when(session.getAttribute("md5Hash")).thenReturn(value);
            } else if ("md5Secret".equals(key)) {
                Mockito.when(session.getAttribute("md5Secret")).thenReturn(value);
            }
            return null;
        }).when(session).setAttribute(Mockito.anyString(), Mockito.any());

        String hash = assignment.getMd5(request);
        String secret = (String) session.getAttribute("md5Secret");

        assertNotNull(secret, "md5Secret should be stored in session");
        assertEquals(
                hash,
                HashingAssignment.getHash(secret, "MD5"),
                "Hash returned by getMd5 must match hash computed from stored secret");
    }

    @RepeatedTest(3)
    @DisplayName("getSha256 should consistently map session-stored secret to returned hash")
    void getSha256_secretAndHashConsistentAcrossCalls() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("sha256")).thenReturn(null);

        Mockito.doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1);
            if ("sha256Hash".equals(key)) {
                Mockito.when(session.getAttribute("sha256Hash")).thenReturn(value);
            } else if ("sha256Secret".equals(key)) {
                Mockito.when(session.getAttribute("sha256Secret")).thenReturn(value);
            }
            return null;
        }).when(session).setAttribute(Mockito.anyString(), Mockito.any());

        String hash = assignment.getSha256(request);
        String secret = (String) session.getAttribute("sha256Secret");

        assertNotNull(secret, "sha256Secret should be stored in session");
        assertEquals(
                hash,
                HashingAssignment.getHash(secret, "SHA-256"),
                "Hash returned by getSha256 must match hash computed from stored secret");
    }
}
