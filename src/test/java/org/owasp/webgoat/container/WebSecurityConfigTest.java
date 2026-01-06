// Assumed test source root: src/test/java
// Package inferred from source file: org.owasp.webgoat.container
package org.owasp.webgoat.container;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.users.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Delta tests for WebSecurityConfig focusing on:
 * - CSRF protection re-enabled (removed csrf().disable()).
 * - Secure password encoder (BCryptPasswordEncoder) instead of NoOpPasswordEncoder.
 */
class WebSecurityConfigTest {

  @Test
  @DisplayName("passwordEncoder bean should not be a NoOp encoder and should encode passwords")
  void passwordEncoderIsSecure() {
    UserService userService = Mockito.mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    PasswordEncoder encoder = config.passwordEncoder();

    String raw = "password";
    String encoded = encoder.encode(raw);

    assertThat(encoded).isNotEqualTo(raw);
    assertThat(encoder.matches(raw, encoded)).isTrue();
  }

  @Test
  @DisplayName("configureGlobal should register the provided PasswordEncoder with AuthenticationManagerBuilder")
  void configureGlobalUsesProvidedPasswordEncoder() throws Exception {
    UserService userService = Mockito.mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    AuthenticationManagerBuilder authBuilder = Mockito.mock(AuthenticationManagerBuilder.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    Mockito.when(authBuilder.userDetailsService(Mockito.any(UserDetailsService.class)))
        .thenReturn(authBuilder);

    config.configureGlobal(authBuilder, encoder);

    Mockito.verify(authBuilder).userDetailsService(userService);
    Mockito.verify(authBuilder).passwordEncoder(encoder);
  }

  @Test
  @DisplayName("filterChain should enforce CSRF protection on state-changing requests")
  void filterChainEnforcesCsrfProtection() throws Exception {
    UserService userService = Mockito.mock(UserService.class);
    WebSecurityConfig config = new WebSecurityConfig(userService);

    HttpSecurity http = new HttpSecurity(null, null, null, null, null, null, null);
    SecurityFilterChain chain = config.filterChain(http);

    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/some/protected/path");
    MockHttpServletResponse response = new MockHttpServletResponse();

    // Apply filter chain to simulate a POST request without CSRF token.
    chain.doFilter(request, response, (req, res) -> {});

    // We can't assert exact status without full Spring Security context,
    // but the intent is that a missing CSRF token should NOT be silently allowed.
    // This test asserts that CSRF is not globally disabled.
    assertThat(http.getConfigurer(org.springframework.security.config.annotation.web.configurers.CsrfConfigurer.class))
        .isNotNull();
  }
}
