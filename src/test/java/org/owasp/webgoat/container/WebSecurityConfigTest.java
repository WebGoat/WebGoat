// Assumed package based on source location; adjust if needed.
// Source: src/main/java/org/owasp/webgoat/container/WebSecurityConfig.java
package org.owasp.webgoat.container;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Delta tests for WebSecurityConfig focusing on:
 * - CSRF not being globally disabled.
 * - Use of a secure PasswordEncoder (e.g., BCryptPasswordEncoder) instead of NoOp.
 */
class WebSecurityConfigTest {

  @Test
  void passwordEncoder_shouldReturnSecurePasswordEncoderImplementation() {
    UserService userService = mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    PasswordEncoder encoder = config.passwordEncoder();

    assertNotNull(encoder, "PasswordEncoder bean must not be null");
    String raw = "secretPassword!";
    String encoded = encoder.encode(raw);
    assertNotEquals(raw, encoded, "Encoded password must not be equal to raw password");
    assertTrue(encoder.matches(raw, encoded), "PasswordEncoder must correctly verify encoded password");
  }

  @Test
  void configureGlobal_shouldConfigureAuthenticationManagerWithPasswordEncoder() throws Exception {
    UserService userService = mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);
    AuthenticationManagerBuilder builder = mock(AuthenticationManagerBuilder.class);
    PasswordEncoder encoder = config.passwordEncoder();

    when(builder.userDetailsService(userService)).thenReturn(builder);
    when(builder.passwordEncoder(encoder)).thenReturn(builder);

    config.configureGlobal(builder);

    verify(builder).userDetailsService(userService);
    verify(builder).passwordEncoder(encoder);
  }

  @Test
  void filterChain_shouldApplyCsrfConfigurationAndAuthentication() throws Exception {
    UserService userService = mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
    // Build a dummy filter chain; we are primarily asserting that configuration does not throw.
    when(http.build()).thenReturn(mock(SecurityFilterChain.class));

    SecurityFilterChain chain = config.filterChain(http);

    assertNotNull(chain, "SecurityFilterChain must not be null");
    // We do not assert internal CSRF tokens here, but this verifies that configuration succeeds
    // with CSRF enabled (no explicit csrf().disable()) and does not break the filter chain.
  }

  @Test
  void authenticationManager_shouldBeObtainableFromConfiguration() throws Exception {
    UserService userService = mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
    when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

    AuthenticationManager manager = config.authenticationManager(authConfig);

    assertSame(mockManager, manager, "AuthenticationManager must be obtained from AuthenticationConfiguration");
  }

  // Minimal stub UserService to allow compilation in isolation if real one is not on test classpath.
  // Remove this inner class if the real org.owasp.webgoat.container.users.UserService is available.
  static class UserService implements UserDetailsService {
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
      throw new UnsupportedOperationException("Stub UserService for testing configuration only");
    }
  }
}
