package org.owasp.webgoat.container.users;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("webgoat-test")
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  void userShouldBeSaved() {
    WebGoatUser user = new WebGoatUser("test", "password");
    userRepository.saveAndFlush(user);

    user = userRepository.findByUsername("test");

    Assertions.assertThat(user.getUsername()).isEqualTo("test");
    Assertions.assertThat(user.getPassword()).isEqualTo("password");
  }
}
