package org.owasp.webgoat.users;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTrackerRepository userTrackerRepository;

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        UserService userService = new UserService(userRepository, userTrackerRepository);
        Assertions.assertThatThrownBy(() -> userService.loadUserByUsername("unknown")).isInstanceOf(UsernameNotFoundException.class);
    }
}