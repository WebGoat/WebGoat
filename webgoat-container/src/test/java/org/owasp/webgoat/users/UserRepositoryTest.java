package org.owasp.webgoat.users;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userShouldBeSaved() {
        WebGoatUser user = new WebGoatUser("test", "password");
        userRepository.saveAndFlush(user);

        user = userRepository.findByUsername("test");

        Assertions.assertThat(user.getUsername()).isEqualTo("test");
        Assertions.assertThat(user.getPassword()).isEqualTo("password");
    }


}