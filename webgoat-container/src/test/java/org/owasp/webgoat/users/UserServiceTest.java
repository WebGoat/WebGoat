package org.owasp.webgoat.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Matchers.any;
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