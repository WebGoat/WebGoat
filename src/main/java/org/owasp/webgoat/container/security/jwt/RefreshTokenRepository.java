package org.owasp.webgoat.container.security.jwt;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

  Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);

  @Modifying
  @Transactional
  @Query(
      "UPDATE RefreshTokenEntity r SET r.revoked = true WHERE r.username = :username AND r.revoked"
          + " = false")
  void revokeAllByUsername(String username);
}
