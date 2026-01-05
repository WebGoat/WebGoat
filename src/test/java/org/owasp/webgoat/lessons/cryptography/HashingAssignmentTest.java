package org.owasp.webgoat.lessons.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

public class HashingAssignmentTest {

    @Test
    void getMd5_shouldReturnSameHashWithinSameSession() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("md5Hash")).thenReturn(null);

        String firstHash = assignment.getMd5(request);

        Mockito.when(session.getAttribute("md5Hash")).thenReturn(firstHash);

        String secondHash = assignment.getMd5(request);

        assertEquals(firstHash, secondHash);
    }

    @RepeatedTest(5)
    void getMd5_shouldTendToReturnDifferentHashesForDifferentSessions() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();

        HttpServletRequest request1 = Mockito.mock(HttpServletRequest.class);
        HttpServletRequest request2 = Mockito.mock(HttpServletRequest.class);
        HttpSession session1 = Mockito.mock(HttpSession.class);
        HttpSession session2 = Mockito.mock(HttpSession.class);

        Mockito.when(request1.getSession()).thenReturn(session1);
        Mockito.when(request2.getSession()).thenReturn(session2);

        Mockito.when(session1.getAttribute("md5Hash")).thenReturn(null);
        Mockito.when(session2.getAttribute("md5Hash")).thenReturn(null);

        String hash1 = assignment.getMd5(request1);
        String hash2 = assignment.getMd5(request2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void getSha256_shouldReturnSameHashWithinSameSession() throws NoSuchAlgorithmException {
        HashingAssignment assignment = new HashingAssignment();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("sha256")).thenReturn(null);

        String firstHash = assignment.getSha256(request);

        Mockito.when(session.getAttribute("sha256")).thenReturn(firstHash);

        String secondHash = assignment.getSha256(request);

        assertEquals(firstHash, secondHash);
    }
}
