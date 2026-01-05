/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

/**
 * @author csabakompes
 * @version $Id: $Id
 */
@Configuration
@EnableWebSecurity
@Order(150)
public class WebSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private void configure(HttpSecurity http) throws Exception {
    http
        .headers(h -> h.defaultsDisabled().cacheControl(Customizer.withDefaults()))
        .authorizeHttpRequests(
            a ->
                a.requestMatchers(
                        new AntPathRequestMatcher("/resources/**"),
                        new AntPathRequestMatcher("/plugins/**"),
                        new AntPathRequestMatcher("/**/favicon.ico"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/images/logos/**"),
                        new RegexRequestMatcher("/webjars/.*", null),
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/signup"))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .anonymous(a -> a.principal("anonymous"))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        .formLogin(
            form ->
                form.loginPage("/login").defaultSuccessUrl("/", true).failureUrl("/login#failed"))
        .logout(
            l ->
                l.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login"))
        .rememberMe(Customizer.withDefaults());
  }

  public SecurityFilterChain web(HttpSecurity http) throws Exception {
    configure(http);
    http.requestMatchers(matcher -> matcher.requestMatchers("/**"));
    return http.build();
  }

  @Configuration
  @Order(20)
  public static class AssignmentEndpointSecurityConfig {

    SecurityFilterChain assignmentEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/assignment/**")
          .csrf(csrf -> csrf
              .csrfTokenRepository(new org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository())
              .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
          .headers(h -> h.frameOptions(f -> f.sameOrigin()))
          .sessionManagement(
              session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return http.build();
    }
  }

  @Configuration
  @Order(10)
  public static class RegistrarEndpointSecurityConfig {

    SecurityFilterChain registerEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/registration/**")
          .anonymous(a -> a.disable())
          .headers(h -> h.frameOptions(f -> f.sameOrigin()))
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER));
      return http.build();
    }
  }

  @Configuration
  @Order(5)
  public static class TraceEndpointSecurityConfig {

    SecurityFilterChain traceEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/trace/**")
          .headers(
              h ->
                  h.frameOptions(f -> f.sameOrigin())
                      .contentSecurityPolicy(c -> c.policyDirectives("default-src 'self'")))
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER));
      return http.build();
    }
  }

  @Configuration
  @Order(1)
  public static class StaticContentEndpointSecurityConfig {

    SecurityFilterChain traceEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/assets/**", "/lessons/**")
          .headers(h -> h.frameOptions(f -> f.sameOrigin()))
          .csrf(csrf -> csrf.disable());
      return http.build();
    }
  }

  @Configuration
  @Order(2)
  public static class GraphQLSecurityConfig {

    SecurityFilterChain traceEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/graphql/**")
          .headers(h -> h.frameOptions(f -> f.sameOrigin()))
          .csrf(csrf -> csrf.disable());
      return http.build();
    }
  }

  @Configuration
  @Order(160)
  public static class AdminEndpointSecurityConfig {

    SecurityFilterChain admEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatcher("/WebGoat/**")
          .authorizeHttpRequests(a -> a.requestMatchers("/WebGoat/**").hasAuthority("ROLE_ADMIN"))
          .headers(h -> h.frameOptions(f -> f.sameOrigin()));
      return http.build();
    }
  }

  @Configuration
  @Order(200)
  public static class ActuatorSecurityConfig {

    public SecurityFilterChain admEndpointSecurityFilterChain(HttpSecurity http) throws Exception {
      if (!ActuatorConfig.isActuatorEnabled()) {
        http.securityMatcher("/actuator/**").authorizeHttpRequests(a -> a.anyRequest().denyAll());
        return http.build();
      }

      http
          .securityMatcher("/actuator/**")
          .authorizeHttpRequests(
              a ->
                  a.requestMatchers("/actuator/**").hasAuthority("ROLE_ADMIN").anyRequest().denyAll())
          .headers(h -> h.frameOptions(f -> f.sameOrigin()));
      return http.build();
    }
  }

  @Configuration
  public static class WebWolfSecurityConfig {

    public SecurityFilterChain webWolfSecurityConfig(HttpSecurity http) throws IOException {
      try {
        http
            .securityMatcher("/webwolf/**")
            .authorizeHttpRequests(
                a ->
                    a.requestMatchers(
                            new AntPathRequestMatcher("/webwolf/**"),
                            new AntPathRequestMatcher("/webwolf/login"))
                        .authenticated())
            .logout(
                l ->
                    l.logoutRequestMatcher(new AntPathRequestMatcher("/webwolf/logout"))
                        .logoutSuccessUrl("/webwolf/login"));
      } catch (Exception e) {
        throw new IOException(e);
      }
      return http.build();
    }
  }
}
