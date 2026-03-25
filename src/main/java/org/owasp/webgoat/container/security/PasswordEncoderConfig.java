package org.owasp.webgoat.container.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

  @Bean
  @SuppressWarnings("deprecation")
  public PasswordEncoder passwordEncoder() {
    // WebGoat intentionally uses NoOpPasswordEncoder for educational purposes
    // Passwords are stored in plaintext to allow lesson demonstrations
    return NoOpPasswordEncoder.getInstance();
  }
}
