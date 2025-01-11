package org.owasp.webgoat.container.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {

  // TODO: make optional
  UserProgress findByUser(String user);
}
