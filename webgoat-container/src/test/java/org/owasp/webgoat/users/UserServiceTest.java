package org.owasp.webgoat.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTrackerRepository userTrackerRepository;


    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenUserIsNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        UserService userService = new UserService(userRepository, userTrackerRepository);
        userService.loadUserByUsername("unknown");
    }
}