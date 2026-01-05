// Assumed package based on source path; adjust if the actual package differs.
package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Delta unit tests for HashingAssignment focusing on the behavior impacted by the fix:
 * - getMd5 and getSha256 should return a stable value per session.
 * - Multiple calls with the same session must not change the hash once set.
 *
 * These tests do not assert the randomness source directly; instead they assert that
 * the observable contract around session-based hash stability is preserved.
 */
public class HashingAssignmentTest {

    @Test
    @DisplayName("getMd5 should return a consistent hash for a given session across multiple calls")
    void testGetMd5ReturnsConsistentHashPerSession() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment hashingAssignment = new HashingAssignment();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        // Simulate session attribute storage using a simple holder
        final Object[] storedHash = new Object[1];
        final Object[] storedSecret = new Object[1];

        Mockito.when(request.getSession()).thenReturn(session);

        Mockito.when(session.getAttribute("md5Hash"))
                .thenAnswer(invocation -> storedHash[0]);
        Mockito.when(session.getAttribute("md5Secret"))
                .thenAnswer(invocation -> storedSecret[0]);

        Mockito.doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1);
            if ("md5Hash".equals(key)) {
                storedHash[0] = value;
            } else if ("md5Secret".equals(key)) {
                storedSecret[0] = value;
            }
            return null;
        }).when(session).setAttribute(Mockito.anyString(), Mockito.any());

        // Act
        String firstHash = hashingAssignment.getMd5(request);
        String secondHash = hashingAssignment.getMd5(request);

        // Assert
        // The same session must see the same hash on subsequent calls once initialized
        assertEquals(firstHash, secondHash, "MD5 hash must remain stable within the same session");
        // Also ensure the cached value in session is the same object reference if returned again
        assertSame(firstHash, secondHash,
                "Subsequent calls should return the same cached hash instance from the session when possible");
    }

    @Test
    @DisplayName("getSha256 should return a consistent hash for a given session across multiple calls")
    void testGetSha256ReturnsConsistentHashPerSession() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment hashingAssignment = new HashingAssignment();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        // Simulate session attribute storage using a simple holder
        final Object[] storedHash = new Object[1];
        final Object[] storedSecret = new Object[1];

        Mockito.when(request.getSession()).thenReturn(session);

        Mockito.when(session.getAttribute("sha256"))
                .thenAnswer(invocation -> storedHash[0]);
        Mockito.when(session.getAttribute("sha256Secret"))
                .thenAnswer(invocation -> storedSecret[0]);

        Mockito.doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1);
            if ("sha256".equals(key)) {
                storedHash[0] = value;
            } else if ("sha256Secret".equals(key)) {
                storedSecret[0] = value;
            }
            return null;
        }).when(session).setAttribute(Mockito.anyString(), Mockito.any());

        // Act
        String firstHash = hashingAssignment.getSha256(request);
        String secondHash = hashingAssignment.getSha256(request);

        // Assert
        assertEquals(firstHash, secondHash, "SHA-256 hash must remain stable within the same session");
        assertSame(firstHash, secondHash,
                "Subsequent calls should return the same cached SHA-256 hash instance from the session when possible");
    }

    @Test
    @DisplayName("getMd5 and getSha256 should operate independently but each remain stable per session")
    void testMd5AndSha256AreIndependentlyConsistentPerSession() throws NoSuchAlgorithmException {
        // Arrange
        HashingAssignment hashingAssignment = new HashingAssignment();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        // Simulate a shared session attribute map
        java.util.Map<String, Object> attributes = new java.util.HashMap<>();

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(Mockito.anyString()))
                .thenAnswer(invocation -> attributes.get(invocation.getArgument(0, String.class)));
        Mockito.doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1);
            attributes.put(key, value);
            return null;
        }).when(session).setAttribute(Mockito.anyString(), Mockito.any());

        // Act
        String md5First = hashingAssignment.getMd5(request);
        String md5Second = hashingAssignment.getMd5(request);

        String shaFirst = hashingAssignment.getSha256(request);
        String shaSecond = hashingAssignment.getSha256(request);

        // Assert
        // Each algorithm must be stable per session across multiple calls
        assertEquals(md5First, md5Second, "MD5 hash must remain stable for the session");
        assertEquals(shaFirst, shaSecond, "SHA-256 hash must remain stable for the session");
    }
}
