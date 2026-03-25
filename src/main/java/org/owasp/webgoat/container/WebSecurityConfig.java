/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * <p>Copyright (c) 2002 - 2014 Bruce Mayhew
 *
 * <p>This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <p>Getting Source ==============
 *
 * <p>Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * <p>
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since December 12, 2015
 */
package org.owasp.webgoat.container;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.security.jwt.JwtAuthenticationFilter;
import org.owasp.webgoat.container.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Security configuration for WebGoat. */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final UserService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public WebSecurityConfig(UserService userDetailsService, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /** Chain 0: Safe deserialization demo endpoint — permit all, no CSRF, no auth */
  @Bean
  @Order(-100)
  public SecurityFilterChain safeDeserializationChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/InsecureDeserialization/safe-task")
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.disable())
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }

  /** Chain 1: JWT API endpoints — stateless, no form login */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/jwt/**").permitAll().anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            ex -> ex.authenticationEntryPoint(
                (request, response, authException) -> {
                  response.setStatus(401);
                  response.setContentType("application/json");
                  response.getWriter().write("{\"error\":\"Unauthorized\"}");
                }))
        .build();
  }

  /** Chain 2: Normal WebGoat UI — form login */
  @Bean
  @Order(2)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/favicon.ico",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "fonts/**",
                        "/plugins/**",
                        "/registration",
                        "/register.mvc",
                        "/InsecureDeserialization/safe-task")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            login ->
                login
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome.mvc", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll())
        .oauth2Login(
            oidc -> {
              oidc.defaultSuccessUrl("/login-oauth.mvc");
              oidc.loginPage("/login");
            })
        .logout(logout -> logout.deleteCookies("JSESSIONID").invalidateHttpSession(true))
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.disable())
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

}
