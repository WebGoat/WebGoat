package org.owasp.webgoat.container.security.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for managing JWT tokens (access and refresh tokens).
 * 
 * This service handles the creation, validation, and revocation of JWT tokens
 * used for authentication and authorization in the WebGoat application.
 * It uses the jjwt 0.9.1 library for JWT operations.
 */
@Service
public class JwtTokenService {

  /**
   * Base64-encoded secret key for signing JWT tokens.
   * Injected from application properties via ${jwt.secret}.
   */
  @Value("${jwt.secret}")
  private String base64Secret;

  /**
   * Expiration time for access tokens in milliseconds.
   * Injected from application properties via ${jwt.access-token-expiration-ms}.
   */
  @Value("${jwt.access-token-expiration-ms}")
  private long accessTokenExpirationMs;

  /**
   * Expiration time for refresh tokens in milliseconds.
   * Injected from application properties via ${jwt.refresh-token-expiration-ms}.
   */
  @Value("${jwt.refresh-token-expiration-ms}")
  private long refreshTokenExpirationMs;

  /**
   * The decoded signing key bytes used for signing and verifying JWT tokens.
   * Initialized in the init() method.
   */
  private byte[] signingKeyBytes;

  /**
   * Repository for persisting and retrieving refresh token entities.
   */
  private final RefreshTokenRepository refreshTokenRepository;

  /**
   * Constructs a JwtTokenService with the given refresh token repository.
   *
   * @param refreshTokenRepository the repository for managing refresh tokens
   */
  public JwtTokenService(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  /**
   * Initializes the service by decoding the base64-encoded secret key
   * and ensuring the key has sufficient length (64 bytes).
   * If the decoded key is shorter than 64 bytes, it is hashed with SHA-512
   * to produce a 64-byte key suitable for HS512 signing.
   * 
   * This method is automatically called by Spring after the bean is constructed
   * and all properties are injected.
   *
   * @throws RuntimeException if the SHA-512 algorithm is not available
   */
  @PostConstruct
  public void init() {
    byte[] decodedSecret = java.util.Base64.getDecoder().decode(base64Secret);
    signingKeyBytes = decodedSecret;
    
    if (signingKeyBytes.length < 64) {
      try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
        signingKeyBytes = digest.digest(signingKeyBytes);
      } catch (java.security.NoSuchAlgorithmException exception) {
        throw new RuntimeException("SHA-512 not available", exception);
      }
    }
  }

  /**
   * Generates a new JWT access token for the given user.
   * 
   * The access token contains the user's username as the subject,
   * the user's role as a custom claim, and is signed with HS512.
   * The token is issued immediately and expires after the configured
   * access token expiration period.
   *
   * @param user the user for whom to generate the access token
   * @return a signed JWT access token string
   */
  public String generateAccessToken(WebGoatUser user) {
    Instant now = Instant.now();
    Date issuedAtDate = Date.from(now);
    Date expirationDate = Date.from(now.plusMillis(accessTokenExpirationMs));
    String username = user.getUsername();
    String role = user.getRole();
    String tokenId = UUID.randomUUID().toString();
    
    String token = Jwts.builder()
        .setSubject(username)
        .claim("role", role)
        .setIssuedAt(issuedAtDate)
        .setExpiration(expirationDate)
        .setIssuer("WebGoat")
        .setId(tokenId)
        .signWith(SignatureAlgorithm.HS512, signingKeyBytes)
        .compact();
    
    return token;
  }

  /**
   * Generates a new refresh token for the given user.
   * 
   * The refresh token is a UUID string that is persisted in the database
   * with the user's username and an expiration timestamp.
   * The token can be used to obtain a new access token without re-authentication.

   *
   * @param user the user for whom to generate the refresh token
   * @return a new refresh token string
   */
  public String generateRefreshToken(WebGoatUser user) {
    String token = UUID.randomUUID().toString();
    Instant expiry = Instant.now().plusMillis(refreshTokenExpirationMs);
    String username = user.getUsername();
    
    RefreshTokenEntity entity = new RefreshTokenEntity(token, username, expiry);
    refreshTokenRepository.save(entity);
    
    return token;
  }

  /**
   * Validates and parses the given JWT access token.
   * 
   * This method verifies the token's signature using the signing key,
   * checks that the issuer is "WebGoat", and ensures the token has not expired.
   * If validation fails, a JwtException is thrown.
   *
   * @param token the JWT access token to validate
   * @return the claims contained in the validated token
   * @throws JwtException if the token is invalid, expired, or has incorrect issuer
   * @throws SignatureException if the signature verification fails
   */
  public Claims validateAccessToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(signingKeyBytes)
        .requireIssuer("WebGoat")
        .parseClaimsJws(token)
        .getBody();
    
    return claims;
  }

  /**
   * Validates the given refresh token.
   * 
   * This method verifies that:
   * 1. The refresh token exists in the database and has not been revoked
   * 2. The token has not expired
   * 
   * If the token is valid but expired, it is marked as revoked before throwing an exception.
   * If the token is valid and not expired, it is marked as revoked (one-time use)
   * and the associated username is returned.
   *
   * @param token the refresh token to validate
   * @return the username associated with the validated refresh token
   * @throws JwtException if the token is not found, already revoked, or expired
   */
  public String validateRefreshToken(String token) {
    Optional<RefreshTokenEntity> optionalEntity = refreshTokenRepository.findByTokenAndRevokedFalse(token);
    
    if (!optionalEntity.isPresent()) {
      throw new JwtException("Refresh token not found or already revoked");
    }
    
    RefreshTokenEntity entity = optionalEntity.get();
    
    if (entity.isExpired()) {
      entity.revoke();
      refreshTokenRepository.save(entity);
      throw new JwtException("Refresh token expired");
    }
    
    entity.revoke();
    refreshTokenRepository.save(entity);
    
    String username = entity.getUsername();
    return username;
  }

  /**
   * Revokes all refresh tokens issued to the given user.
   * 
   * This method marks all non-revoked refresh tokens for the specified user
   * as revoked, effectively logging out the user from all devices/sessions.
   *
   * @param username the username whose tokens should be revoked
   */
  public void revokeAllTokensForUser(String username) {
    refreshTokenRepository.revokeAllByUsername(username);
  }

  /**
   * Extracts the username from a valid JWT access token.
   * 
   * This method first validates the token using validateAccessToken(),
   * then extracts and returns the username from the token's subject claim.
   *
   * @param token the JWT access token
   * @return the username contained in the token's subject claim
   * @throws JwtException if the token is invalid, expired, or has incorrect issuer
   * @throws SignatureException if the signature verification fails
   */
  public String extractUsername(String token) {
    Claims claims = validateAccessToken(token);
    String username = claims.getSubject();
    return username;
  }
}
