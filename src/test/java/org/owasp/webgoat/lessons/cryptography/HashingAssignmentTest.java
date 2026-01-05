package org.owasp.webgoat.lessons.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Delta unit tests for HashingAssignment focusing on the behavior changed by the fix:
 * use of SecureRandom instead of Random for selecting secrets, while preserving:
 *  - non-empty hashes for both endpoints
 *  - secret is always one of HashingAssignment.SECRETS
 *  - session stickiness (same secret/hash reused within a session)
 */
class HashingAssignmentTest {

    private final HashingAssignment hashingAssignment = new HashingAssignment();

    @Test
    @DisplayName("MD5 endpoint returns non-empty hash and uses a secret from SECRETS with session stickiness")
    void testGetMd5_usesValidSecretAndIsStickyPerSession() throws NoSuchAlgorithmException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);

        // First call: should generate and store md5Hash and md5Secret
        String firstHash = hashingAssignment.getMd5(request);
        assertThat(firstHash)
            .as("First MD5 hash must be non-empty")
            .isNotNull()
            .isNotEmpty();

        Object storedHash = session.getAttribute("md5Hash");
        Object storedSecret = session.getAttribute("md5Secret");

        assertThat(storedHash)
            .as("Session must contain md5Hash after first call")
            .isInstanceOf(String.class);
        assertThat(storedSecret)
            .as("Session must contain md5Secret after first call")
            .isInstanceOf(String.class);

        String md5Secret = (String) storedSecret;

        // Secret must always be one of the defined SECRETS (verifies secure random index is bounded)
        Set<String> allowedSecrets = new HashSet<>(Arrays.asList(HashingAssignment.SECRETS));
        assertThat(allowedSecrets)
            .as("Selected md5Secret must be one of HashingAssignment.SECRETS")
            .contains(md5Secret);

        // Second call in same session should not generate a new hash; must return the same value
        String secondHash = hashingAssignment.getMd5(request);
        assertThat(secondHash)
            .as("MD5 hash must be sticky within the same session")
            .isEqualTo(firstHash);

        // Ensure stored hash has not changed
        assertThat(session.getAttribute("md5Hash"))
            .as("md5Hash in session must remain unchanged across calls")
            .isEqualTo(firstHash);
    }

    @Test
    @DisplayName("SHA-256 endpoint returns non-empty hash and uses a secret from SECRETS with session stickiness")
    void testGetSha256_usesValidSecretAndIsStickyPerSession() throws NoSuchAlgorithmException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);

        // First call: should generate and store sha256Hash and sha256Secret
        String firstHash = hashingAssignment.getSha256(request);
        assertThat(firstHash)
            .as("First SHA-256 hash must be non-empty")
            .isNotNull()
            .isNotEmpty();

        Object storedHash = session.getAttribute("sha256Hash");
        Object storedSecret = session.getAttribute("sha256Secret");

        assertThat(storedHash)
            .as("Session must contain sha256Hash after first call")
            .isInstanceOf(String.class);
        assertThat(storedSecret)
            .as("Session must contain sha256Secret after first call")
            .isInstanceOf(String.class);

        String sha256Secret = (String) storedSecret;

        // Secret must always be one of the defined SECRETS (verifies secure random index is bounded)
        Set<String> allowedSecrets = new HashSet<>(Arrays.asList(HashingAssignment.SECRETS));
        assertThat(allowedSecrets)
            .as("Selected sha256Secret must be one of HashingAssignment.SECRETS")
            .contains(sha256Secret);

        // Second call in same session should not generate a new hash; must return the same value
        String secondHash = hashingAssignment.getSha256(request);
        assertThat(secondHash)
            .as("SHA-256 hash must be sticky within the same session")
            .isEqualTo(firstHash);

        // Ensure stored hash has not changed
        assertThat(session.getAttribute("sha256Hash"))
            .as("sha256Hash in session must remain unchanged across calls")
            .isEqualTo(firstHash);
    }

    @Test
    @DisplayName("MD5 endpoint does not regenerate hash when session already has md5Hash")
    void testGetMd5_doesNotRegenerateWhenSessionPrepopulated() throws NoSuchAlgorithmException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);

        // Pre-populate session to simulate existing hash/secret
        session.setAttribute("md5Hash", "PRECOMPUTED_HASH");
        session.setAttribute("md5Secret", "secret");

        String result = hashingAssignment.getMd5(request);

        // Should simply return the existing hash without touching the secret
        assertThat(result).isEqualTo("PRECOMPUTED_HASH");
        assertThat(session.getAttribute("md5Secret")).isEqualTo("secret");
    }

    @Test
    @DisplayName("SHA-256 endpoint does not regenerate hash when session already has sha256Hash")
    void testGetSha256_doesNotRegenerateWhenSessionPrepopulated() throws NoSuchAlgorithmException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);

        // Pre-populate session to simulate existing hash/secret
        session.setAttribute("sha256Hash", "PRECOMPUTED_SHA256");
        session.setAttribute("sha256Secret", "admin");

        String result = hashingAssignment.getSha256(request);

        // Should simply return the existing hash without touching the secret
        assertThat(result).isEqualTo("PRECOMPUTED_SHA256");
        assertThat(session.getAttribute("sha256Secret")).isEqualTo("admin");
    }

    @Test
    @DisplayName("Secrets stored in session are consistent with returned hashes")
    void testSecretsAndHashesAreConsistent() throws NoSuchAlgorithmException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);

        // Exercise both endpoints to populate session
        String md5Hash = hashingAssignment.getMd5(request);
        String sha256Hash = hashingAssignment.getSha256(request);

        String md5Secret = (String) session.getAttribute("md5Secret");
        String sha256Secret = (String) session.getAttribute("sha256Secret");

        // Recompute using helper to ensure consistency
        String recomputedMd5 = HashingAssignment.getHash(md5Secret, "MD5");
        String recomputedSha256 = HashingAssignment.getHash(sha256Secret, "SHA-256");

        assertThat(md5Hash)
            .as("Stored MD5 hash must match recomputed hash from stored secret")
            .isEqualTo(recomputedMd5);
        assertThat(sha256Hash)
            .as("Stored SHA-256 hash must match recomputed hash from stored secret")
            .isEqualTo(recomputedSha256);
    }
}
