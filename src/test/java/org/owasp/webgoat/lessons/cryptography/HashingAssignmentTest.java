package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class HashingAssignmentTest {

    @Test
    @DisplayName("getMd5 should generate and cache MD5 hash and secret in session when not present")
    void getMd5_generatesAndCachesHashAndSecret() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("md5Hash")).thenReturn(null);

        String firstHash = assignment.getMd5(request);

        assertNotNull(firstHash, "First MD5 hash must not be null");
        verify(session).setAttribute(eq("md5Hash"), eq(firstHash));
        verify(session).setAttribute(eq("md5Secret"), anyString());
    }

    @Test
    @DisplayName("getMd5 should return cached hash if it already exists in session")
    void getMd5_returnsCachedHashIfPresent() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        String cachedHash = "ABCDEF0123456789";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("md5Hash")).thenReturn(cachedHash);

        String result = assignment.getMd5(request);

        assertEquals(cachedHash, result, "Should return the cached MD5 hash when it exists");
        verify(session, never()).setAttribute(eq("md5Hash"), any());
        verify(session, never()).setAttribute(eq("md5Secret"), any());
    }

    @Test
    @DisplayName("getSha256 should generate and cache SHA-256 hash and secret in session when not present")
    void getSha256_generatesAndCachesHashAndSecret() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("sha256")).thenReturn(null);

        String firstHash = assignment.getSha256(request);

        assertNotNull(firstHash, "First SHA-256 hash must not be null");
        verify(session).setAttribute(eq("sha256Hash"), eq(firstHash));
        verify(session).setAttribute(eq("sha256Secret"), anyString());
    }

    @Test
    @DisplayName("getSha256 should return cached hash if it already exists in session")
    void getSha256_returnsCachedHashIfPresent() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        String cachedHash = "ABCDEF0123456789";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("sha256")).thenReturn(cachedHash);

        String result = assignment.getSha256(request);

        assertEquals(cachedHash, result, "Should return the cached SHA-256 hash when it exists");
        verify(session, never()).setAttribute(eq("sha256Hash"), any());
        verify(session, never()).setAttribute(eq("sha256Secret"), any());
    }

    @RepeatedTest(3)
    @DisplayName("getMd5 should always use one of the allowed secrets (SecureRandom-based selection)")
    void getMd5_usesOnlyPredefinedSecrets() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("md5Hash")).thenReturn(null);

        assignment.getMd5(request);

        verify(session).setAttribute(eq("md5Secret"), argThat(secret -> {
            assertNotNull(secret, "Secret stored in session must not be null");
            boolean found = false;
            for (String allowed : HashingAssignment.SECRETS) {
                if (allowed.equals(secret)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Secret must be one of the predefined SECRETS");
            return true;
        }));
    }
}
