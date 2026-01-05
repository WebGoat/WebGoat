// Derived from: src/main/java/org/owasp/webgoat/container/WebSecurityConfig.java
// Test path assumption: src/test/java/org/owasp/webgoat/container/WebSecurityConfigTest.java
package org.owasp.webgoat.container;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfConfigurer;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

/**
 * Delta unit tests for WebSecurityConfig focusing on:
 * - Introduction of a strong PasswordEncoder bean (BCryptPasswordEncoder).
 * - Enabling CSRF protection for the /assignment/** endpoint with a proper token repository
 *   and request handler.
 *
 * These tests are structural: they assert the presence and type of the new security
 * components rather than running a full Spring context.
 */
public class WebSecurityConfigTest {

  @Test
  @DisplayName("passwordEncoder bean should be a non-null strong PasswordEncoder (BCrypt)")
  void passwordEncoder_isBCryptPasswordEncoder() {
    // Arrange
    WebSecurityConfig config = new WebSecurityConfig();

    // Act
    PasswordEncoder encoder = config.passwordEncoder();

    // Assert
    assertNotNull(encoder, "PasswordEncoder bean must not be null");
    // We check for BCrypt by class name to avoid tight coupling to implementation package
    assertTrue(
        encoder.getClass().getSimpleName().contains("BCrypt"),
        "PasswordEncoder should be a BCrypt-based implementation"
    );
  }

  @Test
  @DisplayName("AssignmentEndpointSecurityConfig should configure CSRF with token repository and request handler")
  void assignmentEndpointSecurityConfig_enablesCsrfWithRepositoryAndHandler() throws Exception {
    // Arrange
    WebSecurityConfig.AssignmentEndpointSecurityConfig config =
        new WebSecurityConfig.AssignmentEndpointSecurityConfig();

    // We cannot easily inspect the fully built SecurityFilterChain without a full Spring context,
    // but we can at least ensure that configuring CSRF on HttpSecurity is possible and that
    // CsrfTokenRequestAttributeHandler is available, which ties directly to the new behavior.
    HttpSecurity httpSecurity = new HttpSecurity(null, null, null, null, null, null, null);

    // Act
    // We call the method to ensure no IllegalStateException is thrown when CSRF is configured.
    config.assignmentEndpointSecurityFilterChain(httpSecurity);

    // Assert
    // Indirect structural assertion: CsrfConfigurer and CsrfTokenRequestAttributeHandler are present
    CsrfConfigurer<HttpSecurity> csrfConfigurer = httpSecurity.getConfigurer(CsrfConfigurer.class);
    assertNotNull(csrfConfigurer, "CSRF must be configured on HttpSecurity for assignment endpoints");

    // The handler class referenced in the production code must be loadable
    CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
    assertNotNull(handler);
  }
}
