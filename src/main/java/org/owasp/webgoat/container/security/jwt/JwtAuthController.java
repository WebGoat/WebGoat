package org.owasp.webgoat.container.security.jwt;

import io.jsonwebtoken.JwtException;
import java.util.HashMap;
import java.util.Map;
import org.owasp.webgoat.container.users.UserService;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for JWT-based authentication.
 * 
 * Provides endpoints for user login, token refresh, and logout operations.
 * All authentication is performed using JWT tokens with refresh token rotation.
 */
@RestController
@RequestMapping("/api/jwt")
public class JwtAuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  /**
   * Constructs a new JwtAuthController with the required dependencies.
   *
   * @param authenticationManager the Spring Security authentication manager
   * @param jwtTokenService the service for generating and validating JWT tokens
   * @param userService the service for loading user data
   */
  public JwtAuthController(
      AuthenticationManager authenticationManager,
      JwtTokenService jwtTokenService,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenService = jwtTokenService;
    this.userService = userService;
  }

  /**
   * Authenticates a user with username and password credentials and returns JWT tokens.
   * 
   * This endpoint accepts a JSON body containing the user's username and password.
   * Upon successful authentication, it returns an access token and a refresh token.
   * The access token has an expiration time of 900 seconds (15 minutes).
   *
   * @param credentials a map containing "username" and "password" keys
   * @return a ResponseEntity containing the access token, refresh token, token type, and expiration time,
   *         or an error response if authentication fails
   */
  @PostMapping(
      value = "/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");

    if (username == null || password == null) {
      HashMap<String, Object> errorResponse = new HashMap<String, Object>();
      errorResponse.put("error", "Username and password are required");
      return ResponseEntity.badRequest().body(errorResponse);
    }

    try {
      UsernamePasswordAuthenticationToken authToken = 
          new UsernamePasswordAuthenticationToken(username, password);
      Authentication authentication = authenticationManager.authenticate(authToken);

      WebGoatUser user = (WebGoatUser) authentication.getPrincipal();

      String accessToken = jwtTokenService.generateAccessToken(user);
      String refreshToken = jwtTokenService.generateRefreshToken(user);

      HashMap<String, Object> response = new HashMap<String, Object>();
      response.put("access_token", accessToken);
      response.put("refresh_token", refreshToken);
      response.put("token_type", "Bearer");
      response.put("expires_in", 900);
      
      return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
      HashMap<String, Object> errorResponse = new HashMap<String, Object>();
      errorResponse.put("error", "Invalid username or password");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
  }

  /**
   * Refreshes an expired or expiring access token using a refresh token.
   * 
   * This endpoint performs refresh token rotation: the old refresh token is invalidated
   * and a new refresh token is issued along with a new access token. This approach
   * prevents token reuse attacks.
   *
   * @param body a map containing the "refresh_token" key
   * @return a ResponseEntity containing new access and refresh tokens, or an error response
   *         if the refresh token is invalid or expired
   */
  @PostMapping(
      value = "/refresh",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
    String refreshToken = body.get("refresh_token");

    if (refreshToken == null) {
      HashMap<String, Object> errorResponse = new HashMap<String, Object>();
      errorResponse.put("error", "Refresh token is required");
      return ResponseEntity.badRequest().body(errorResponse);
    }

    try {
      String username = jwtTokenService.validateRefreshToken(refreshToken);

      WebGoatUser user = userService.loadUserByUsername(username);

      String newAccessToken = jwtTokenService.generateAccessToken(user);
      String newRefreshToken = jwtTokenService.generateRefreshToken(user);

      HashMap<String, Object> response = new HashMap<String, Object>();
      response.put("access_token", newAccessToken);
      response.put("refresh_token", newRefreshToken);
      response.put("token_type", "Bearer");
      response.put("expires_in", 900);
      
      return ResponseEntity.ok(response);

    } catch (JwtException e) {
      HashMap<String, Object> errorResponse = new HashMap<String, Object>();
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
  }

  /**
   * Logs out the authenticated user by revoking all their tokens.
   * 
   * This endpoint invalidates all refresh tokens for the authenticated user and
   * clears the security context. The user must provide a valid JWT access token
   * in the Authorization header.
   *
   * @return a ResponseEntity containing a success message if logout is successful,
   *         or an error response if the user is not authenticated
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.getPrincipal() instanceof WebGoatUser) {
      WebGoatUser user = (WebGoatUser) authentication.getPrincipal();
      jwtTokenService.revokeAllTokensForUser(user.getUsername());
      SecurityContextHolder.clearContext();
      
      HashMap<String, Object> response = new HashMap<String, Object>();
      response.put("message", "Logged out successfully");
      return ResponseEntity.ok(response);
    }

    HashMap<String, Object> errorResponse = new HashMap<String, Object>();
    errorResponse.put("error", "Not authenticated");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }
}
