package org.owasp.webgoat.container.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.owasp.webgoat.container.users.UserService;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserService userService) {
    this.jwtTokenService = jwtTokenService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    // Only process if Bearer token is present and no existing authentication
    if (authHeader != null
        && authHeader.startsWith("Bearer ")
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      String token = authHeader.substring(7);

      try {
        Claims claims = jwtTokenService.validateAccessToken(token);
        String username = claims.getSubject();

        WebGoatUser user = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (JwtException e) {
        // Invalid token - don't set authentication, let the filter chain continue
        // The security config will handle unauthorized access
        logger.debug("JWT validation failed: " + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.startsWith("/api/jwt/");
  }
}
