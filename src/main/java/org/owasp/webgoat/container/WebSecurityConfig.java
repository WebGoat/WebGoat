/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import static org.owasp.webgoat.container.session.User.USER_ROLE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean; // Added import for @Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Added import for BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Added import for PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler; // Added import for CsrfTokenRequestAttributeHandler

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  @Configuration
  @Order(1)
  public static class WebWolfSecurityConfig {

    private final WebWolfAuthenticationProviderImpl authProvider;

    @Configuration
    @RequiredArgsConstructor
    public static class WebWolfAuthSecurityConfig {

      private final WebWolfAuthenticationProviderImpl authProvider;

      @SuppressWarnings("java:S4502")
      @Bean
      public SecurityFilterChain webWolfSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/WebWolf/**");
        http.authorizeHttpRequests(
                (requests) ->
                    requests
                        .requestMatchers(
                            "/WebWolf/error",
                            "/WebWolf/css/**",
                            "/WebWolf/images/**",
                            "/WebWolf/js/**",
                            "/WebWolf/fonts/**",
                            "/WebWolf/mail/**",
                            "/WebWolf/account/**",
                            "/WebWolf/getCookie.mvc",
                            "/WebWolf/trace/**")
                        .permitAll()
                        .requestMatchers("/**/css/**", "/**/images/**", "/**/js/**")
                        .permitAll()
                        .requestMatchers(
                            "/WebWolf/vulnerabilities/unvalidated-forwards/mail",
                            "/WebWolf/vulnerabilities/unvalidated-forwards/helloworld")
                        .permitAll()
                        .requestMatchers("/WebWolf/**")
                        .hasAnyRole("WEBWOLF_USER", "ADMIN")
                        .anyRequest()
                        .authenticated())
            .formLogin(
                (login) ->
                    login.loginPage("/WebWolf/login").defaultSuccessUrl("/WebWolf/mail", true))
            .logout((logout) -> logout.logoutUrl("/WebWolf/logout").logoutSuccessUrl("/WebWolf"));
        // Remediation: Removed AbstractHttpConfigurer::disable to enable CSRF protection
        // http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())); // Added CsrfTokenRequestAttributeHandler
        http.authenticationProvider(authProvider);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
      }
    }
  }

  // Remediation: Added a secure PasswordEncoder bean
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(3)
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**", "/service/**");
    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/v2/attack",
                        "/service/scores",
                        "/service/lessonoverview/")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/service/event/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v2/attack")
                    .hasRole(USER_ROLE)
                    .requestMatchers("/service/lessonmenu.mvc")
                    .hasRole(USER_ROLE)
                    .requestMatchers("/service/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());
    // Remediation: Removed AbstractHttpConfigurer::disable to enable CSRF protection
    // http.csrf(AbstractHttpConfigurer::disable);
    http.csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())); // Added CsrfTokenRequestAttributeHandler

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/**");
    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(
                        "/register.mvc",
                        "/login.mvc",
                        "/plugin_encrypted.jsp",
                        "/plugins/csrf/encrypted",
                        "/password-reset",
                        "/password-change",
                        "/password-change-success",
                        "/error",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/fonts/**",
                        "/actuator/**")
                    .permitAll()
                    .requestMatchers("/**")
                    .hasAnyRole(USER_ROLE, "ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(
            (login) ->
                login.loginPage("/login.mvc").defaultSuccessUrl("/", true).permitAll())
        .logout((logout) -> logout.logoutUrl("/logout.mvc").logoutSuccessUrl("/login.mvc"));
    // Remediation: Removed AbstractHttpConfigurer::disable to enable CSRF protection
    // http.csrf(AbstractHttpConfigurer::disable);
    http.csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())); // Added CsrfTokenRequestAttributeHandler

    return http.build();
  }
}
