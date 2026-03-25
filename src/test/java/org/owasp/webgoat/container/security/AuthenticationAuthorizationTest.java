package org.owasp.webgoat.container.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Module 6: Authentication and Authorization Integration Tests
 * 
 * This test class verifies that authentication and authorization security controls
 * are correctly implemented in WebGoat. These tests ensure that the application
 * properly protects sensitive endpoints and resources from unauthorized access.
 * 
 * Key Security Principles Tested:
 * - AUTHENTICATION: Verifying user identity before granting access
 * - AUTHORIZATION: Verifying user permissions for specific resources
 * - REDIRECT ENFORCEMENT: Unauthenticated users are redirected to login
 * - PUBLIC RESOURCES: Login and static assets remain accessible
 * 
 * References:
 * - Spring Security Testing: https://docs.spring.io/spring-security/reference/servlet/test/
 * - OWASP Testing Guide - Authentication Testing (OTG-AUTHN)
 * - OWASP Testing Guide - Authorization Testing (OTG-AUTHZ)
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    // ─────────────────────────────────────────────────────────────────────────
    // AUTHENTICATION TESTS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TEST 1: Unauthenticated access to protected page redirects to login.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that when an unauthenticated user attempts to access
     * a protected resource (/welcome.mvc), the Spring Security filter chain
     * intercepts the request and redirects to the login page.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * This is a critical security control. If unauthenticated users could access
     * protected resources, the application would fail to enforce authentication,
     * allowing attackers to view sensitive information without credentials.
     * The HTTP 3xx redirect status code ensures the redirect is automatic.
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 3xx Redirection (typically 302 Found)
     * - Location Header: Should contain login URL pattern (matches /login)
     * - User should be redirected transparently without seeing sensitive content
     */
    @Test
    @DisplayName("T1: Unauthenticated access to protected page redirects to login")
    void unauthenticatedAccessRedirectsToLogin() throws Exception {
        // Attempt to access protected resource without authentication
        ResultActions result = mockMvc.perform(get("/welcome.mvc"));
        
        // Verify the response is a redirection
        result.andExpect(status().is3xxRedirection());
        
        // Verify the redirect location points to login endpoint
        result.andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * TEST 2: Login page is publicly accessible.
     * 
     * WHAT IS BEING TESTED:
     * This test confirms that the login page itself is accessible to anyone,
     * including unauthenticated users. This is necessary so users can log in.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * If the login page were behind authentication, legitimate users couldn't
     * authenticate in the first place (a logical impossibility). The login form
     * must be publicly accessible, but the login process itself must be protected
     * with CSRF tokens (tested separately).
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - Login form should be rendered and visible to unauthenticated users
     */
    @Test
    @DisplayName("T2: Login page is publicly accessible")
    void loginPageIsPubliclyAccessible() throws Exception {
        // Attempt to access login page without authentication
        ResultActions result = mockMvc.perform(get("/login"));
        
        // Verify successful response
        result.andExpect(status().isOk());
    }

    /**
     * TEST 3: Registration page is publicly accessible.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that new users can access the registration page
     * without being authenticated. This is necessary for new user onboarding.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * Like the login page, the registration endpoint must be public to allow
     * new users to create accounts. However, the registration process should
     * still be protected against abuse (e.g., via CAPTCHA or rate limiting,
     * though not tested here).
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - Registration form should be rendered
     */
    @Test
    @DisplayName("T3: Registration page is publicly accessible")
    void registrationPageIsPubliclyAccessible() throws Exception {
        // Attempt to access registration page without authentication
        ResultActions result = mockMvc.perform(get("/registration"));
        
        // Verify successful response
        result.andExpect(status().isOk());
    }

    /**
     * TEST 4: Invalid login credentials are rejected.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that the login endpoint properly rejects incorrect
     * credentials. It uses a POST request with intentionally wrong username
     * and password, and includes a CSRF token (required for POST requests).
     * 
     * WHY IT MATTERS FOR SECURITY:
     * This test ensures the application doesn't grant access to users with
     * incorrect credentials. A failed authentication attempt should:
     * 1. Not create a valid session
     * 2. Redirect back to login with an error indicator
     * 3. Not provide information about which credential was wrong
     *    (to prevent username enumeration attacks)
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 3xx Redirection (typically 302)
     * - Location: /login?error (indicates authentication failure)
     * - No session is created for the failed login attempt
     */
    @Test
    @DisplayName("T4: Invalid login credentials are rejected")
    void invalidCredentialsAreRejected() throws Exception {
        // Attempt to log in with non-existent user and wrong password
        ResultActions result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "nonexistent_user_xyz")
                .param("password", "wrong_password_xyz")
                .with(csrf()));  // Include CSRF token to satisfy Spring Security
        
        // Verify authentication failed and user is redirected
        result.andExpect(status().is3xxRedirection());
        
        // Verify redirect includes error parameter
        result.andExpect(redirectedUrl("/login?error"));
    }

    /**
     * TEST 5: Authenticated user can access protected resources.
     * 
     * WHAT IS BEING TESTED:
     * This test uses @WithMockUser to simulate an authenticated user session.
     * It verifies that once a user is authenticated, they can access protected
     * resources like the welcome page.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * This is the positive test case that confirms authentication works correctly.
     * It ensures that:
     * 1. Valid credentials grant access to protected resources
     * 2. The authentication mechanism doesn't reject legitimate users
     * 3. The Spring Security filter chain allows authenticated requests through
     * 
     * The @WithMockUser annotation simulates a real user with "USER" role.
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - Authenticated user receives the welcome page content
     * - No redirect occurs for authenticated requests
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("T5: Authenticated user can access protected resources")
    void authenticatedUserCanAccessProtectedResources() throws Exception {
        // Attempt to access protected welcome page as authenticated user
        ResultActions result = mockMvc.perform(get("/welcome.mvc"));
        
        // Verify access is granted
        result.andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AUTHORIZATION TESTS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TEST 6: Unauthenticated access to lesson endpoint is blocked.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that lesson-specific endpoints (like /SqlInjection/attack2)
     * require authentication. Even though we're sending a POST request with
     * parameters, without authentication the request should be rejected.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * Lesson endpoints often contain sensitive educational content about
     * vulnerabilities. They may also trigger code execution or database queries.
     * Allowing unauthenticated access could:
     * 1. Leak learning material to unauthorized users
     * 2. Expose the application to malicious input without user verification
     * 3. Allow attackers to use lesson endpoints for actual exploitation
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 3xx Redirection (typically 302)
     * - Location: Should redirect to login endpoint
     * - Request parameters are not processed
     */
    @Test
    @DisplayName("T6: Unauthenticated access to lesson endpoint is blocked")
    void unauthenticatedAccessToLessonIsBlocked() throws Exception {
        // Attempt to POST to a lesson endpoint without authentication
        ResultActions result = mockMvc.perform(post("/SqlInjection/attack2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("query", "SELECT * FROM users"));
        
        // Verify the request is blocked with redirect
        result.andExpect(status().is3xxRedirection());
        
        // Verify redirect points to login
        result.andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * TEST 7: Authenticated user can access the start page.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that authenticated users can access the start page
     * (/start.mvc) which is typically the main landing page after login.
     * It also verifies that the application accepts HTML content negotiation.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * This ensures the happy path works: once authentication succeeds, users
     * should be able to navigate the application normally. This test confirms:
     * 1. Authentication state is properly maintained
     * 2. The application can serve HTML content to authenticated users
     * 3. No internal errors occur during page rendering
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - Start page is rendered and returned to the authenticated user
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("T7: Authenticated user can access the start page")
    void authenticatedUserCanAccessStartPage() throws Exception {
        // Attempt to access start page as authenticated user
        ResultActions result = mockMvc.perform(get("/start.mvc")
                .accept(MediaType.TEXT_HTML));
        
        // Verify access is granted
        result.andExpect(status().isOk());
    }

    /**
     * TEST 8: Static resources (CSS, JS) are publicly accessible.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that static web resources (CSS, JavaScript, images, etc.)
     * are publicly accessible without authentication. This is critical because
     * the login page itself needs these resources to render correctly.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * If static resources required authentication:
     * 1. The login page would be blank (CSS not loaded) — unusable
     * 2. Users couldn't log in — bootstrap problem
     * 3. The application would be inaccessible to everyone
     * 
     * Static resources should be publicly accessible but should ideally:
     * - Come from a content delivery network (CDN) with caching headers
     * - Include integrity checks (subresource integrity)
     * - Be free from injection vulnerabilities
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - Static resource (CSS file) is returned
     */
    @Test
    @DisplayName("T8: Static resources are publicly accessible")
    void staticResourcesArePubliclyAccessible() throws Exception {
        // Attempt to access a static CSS file without authentication
        ResultActions result = mockMvc.perform(get("/css/main.css"));
        
        // Verify static resource is accessible
        result.andExpect(status().isOk());
    }

    /**
     * TEST 9: Safe deserialization endpoint is accessible.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that the safe deserialization endpoint
     * (/InsecureDeserialization/safe-task) is accessible without authentication.
     * This is a whitelisted endpoint that allows public access to a safe operation.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * In some cases, applications need to provide limited functionality without
     * requiring full authentication. This test ensures that:
     * 1. Security configuration correctly whitelists this endpoint
     * 2. The safe endpoint can process requests from unauthenticated users
     * 3. Access control rules don't inadvertently block safe operations
     * 
     * However, this endpoint still receives user input and must validate/sanitize
     * it properly to prevent injection attacks.
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 200 OK
     * - The endpoint processes the request and returns a response
     */
    @Test
    @DisplayName("T9: Safe deserialization endpoint is accessible")
    void safeDeserializationEndpointIsAccessible() throws Exception {
        // Attempt to POST to safe deserialization endpoint without authentication
        ResultActions result = mockMvc.perform(post("/InsecureDeserialization/safe-task")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("token", "dGVzdA=="));
        
        // Verify endpoint is accessible
        result.andExpect(status().isOk());
    }

    /**
     * TEST 10: Unauthenticated POST to lesson endpoint is blocked.
     * 
     * WHAT IS BEING TESTED:
     * This test verifies that POST requests to lesson assignment endpoints
     * (like /InsecureDeserialization/task) require authentication. This is the
     * state-changing variant of the lesson endpoint test.
     * 
     * WHY IT MATTERS FOR SECURITY:
     * POST requests typically modify application state (database changes, file writes).
     * Allowing unauthenticated POST requests to lesson endpoints could:
     * 1. Corrupt the learning progress of authenticated users
     * 2. Allow unauthorized changes to application data
     * 3. Enable attackers to exploit lesson endpoints for real harm
     * 
     * Additionally, POST requests should be protected by CSRF tokens when they
     * come from authenticated sessions (though this endpoint requires auth first).
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP Status: 3xx Redirection (typically 302)
     * - Location: Should redirect to login endpoint
     * - No state changes occur for unauthenticated requests
     */
    @Test
    @DisplayName("T10: Unauthenticated POST to lesson endpoint is blocked")
    void unauthenticatedPostToLessonEndpointIsBlocked() throws Exception {
        // Attempt to POST to lesson endpoint without authentication
        ResultActions result = mockMvc.perform(post("/InsecureDeserialization/task")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("token", "dGVzdA=="));
        
        // Verify the request is blocked with redirect
        result.andExpect(status().is3xxRedirection());
        
        // Verify redirect points to login
        result.andExpect(redirectedUrlPattern("**/login"));
    }
}
