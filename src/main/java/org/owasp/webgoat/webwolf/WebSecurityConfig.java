/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.AjaxAuthenticationEntryPoint;
import org.owasp.webgoat.webwolf.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** Security configuration for WebWolf. */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

  private final UserService userDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            auth -> {
              auth.requestMatchers("/css/**", "/webjars/**", "/favicon.ico", "/js/**", "/images/**")
                  .permitAll();
              auth.requestMatchers(
                      HttpMethod.GET,
                      "/fileupload/**",
                      "/files/**",
                      "/landing/**",
                      "/PasswordReset/**")
                  .permitAll();
              auth.requestMatchers(HttpMethod.POST, "/files", "/mail", "/requests").permitAll();
              auth.anyRequest().authenticated();
            })
        .csrf(csrf -> csrf.disable())
        .formLogin(
            login ->
                login
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/home", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll())
        .oauth2Login(
            oidc -> {
              oidc.defaultSuccessUrl("/home");
            })
        .logout(logout -> logout.deleteCookies("WEBWOLFSESSION").invalidateHttpSession(true))
        .exceptionHandling(
            handling ->
                handling.authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/login")))
        .build();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Bean
  public UserDetailsService userDetailsServiceBean() {
    return userDetailsService;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public NoOpPasswordEncoder passwordEncoder() {
    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
  }
}
