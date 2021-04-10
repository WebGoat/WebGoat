package org.owasp.webgoat.users;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        UserService userService = new UserService(userRepository, userTrackerRepository, jdbcTemplate, flywayLessons);
        Assertions.assertThatThrownBy(() -> userService.loadUserByUsername("unknown")).isInstanceOf(UsernameNotFoundException.class);
    }
}