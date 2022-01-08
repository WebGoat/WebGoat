package org.owasp.webgoat.container.users;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Initializeable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTrackerRepository userTrackerRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private Function<String, Flyway> flywayLessons;

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        UserService userService = new UserService(userRepository, userTrackerRepository, jdbcTemplate, flywayLessons, List.of());
        Assertions.assertThatThrownBy(() -> userService.loadUserByUsername("unknown")).isInstanceOf(UsernameNotFoundException.class);
    }
}
