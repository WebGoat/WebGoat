/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.session.WebGoatSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Added import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final WebGoatUserDetailsService userDetailsService;
  private final WebGoatSession webGoatSession;

  @Bean
  public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {

    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder);
    return authProvider;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.addFilterAfter(new UserTrackerFilter(webGoatSession), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.requestMatchers("/css/**",
                "/images/logos/**",
                "/js/**",
                "/plugins/fonts/**",
                "/webjars/**",
                "/actuator/health",
                "/register.mvc",
                "/register",
                "/registration",
                "/login**",
                "/lessonoverview.mvc",
                "/h2-console/**").permitAll()
                .requestMatchers("/**").authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(Customizer.withDefaults())
        .formLogin(form ->
            form.loginPage("/login").loginProcessingUrl("/login.mvc").permitAll())
        .sessionManagement(sessionManagement ->
            sessionManagement.invalidSessionUrl("/login.mvc?error=invalidSession").maximumSessions(1))
        .logout(logout ->
            logout.logoutSuccessUrl("/login.mvc"));

    http.headers(headers -> headers.frameOptions(Customizer.withDefaults()).cacheControl(Customizer.withDefaults()));

    return http.build();
  }
}
