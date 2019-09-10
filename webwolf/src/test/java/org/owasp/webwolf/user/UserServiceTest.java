package org.owasp.webwolf.user;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserService sut;

    @Test
    public void testLoadUserByUsername(){
        var username = "guest";
        var password = "123";
        WebGoatUser user = new WebGoatUser(username, password);
        when(mockUserRepository.findByUsername(username)).thenReturn(user);

        var webGoatUser = sut.loadUserByUsername(username);

        Assertions.assertThat(username).isEqualTo(webGoatUser.getUsername());
        Assertions.assertThat(password).isEqualTo(webGoatUser.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_NULL(){
        var username = "guest";
        when(mockUserRepository.findByUsername(username)).thenReturn(null);

        sut.loadUserByUsername(username);
    }

    @Test
    public void testAddUser(){
        var username = "guest";
        var password = "guest";

        sut.addUser(username, password);

        verify(mockUserRepository, times(1)).save(any(WebGoatUser.class));
    }
}
