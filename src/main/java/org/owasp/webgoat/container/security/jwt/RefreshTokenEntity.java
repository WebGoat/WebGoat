package org.owasp.webgoat.container.security.jwt;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private Instant expiryDate;

  @Column(nullable = false)
  private boolean revoked = false;

  protected RefreshTokenEntity() {}

  public RefreshTokenEntity(String token, String username, Instant expiryDate) {
    this.token = token;
    this.username = username;
    this.expiryDate = expiryDate;
  }

  public Long getId() {
    return id;
  }

  public String getToken() {
    return token;
  }

  public String getUsername() {
    return username;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void revoke() {
    this.revoked = true;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(this.expiryDate);
  }
}
