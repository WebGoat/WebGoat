package org.owasp.webgoat.container;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

class WebSecurityConfigTest {

  @Test
  @DisplayName("passwordEncoder bean should be a BCrypt-based PasswordEncoder")
  void passwordEncoder_isBcrypt() {
    WebSecurityConfig config = new WebSecurityConfig();

    PasswordEncoder encoder = config.passwordEncoder();

    assertNotNull(encoder, "PasswordEncoder bean must not be null");
    String hashed = encoder.encode("password");
    assertTrue(
        encoder.matches("password", hashed),
        "BCrypt PasswordEncoder should correctly match encoded password");
  }

  @Test
  @DisplayName("apiSecurityFilterChain should not globally disable CSRF")
  void apiSecurityFilterChain_csrfNotDisabled() throws Exception {
    WebSecurityConfig config = new WebSecurityConfig();
    HttpSecurity http = new HttpSecurity(null, null, null, null, null, null, null);

    SecurityFilterChain chain = config.apiSecurityFilterChain(http);

    assertNotNull(chain, "SecurityFilterChain for API must be created");
  }

  @Test
  @DisplayName("userSecurityFilterChain should not globally disable CSRF")
  void userSecurityFilterChain_csrfNotDisabled() throws Exception {
    WebSecurityConfig config = new WebSecurityConfig();
    HttpSecurity http = new HttpSecurity(null, null, null, null, null, null, null);

    SecurityFilterChain chain = config.userSecurityFilterChain(http);

    assertNotNull(chain, "SecurityFilterChain for user endpoints must be created");
  }
}
