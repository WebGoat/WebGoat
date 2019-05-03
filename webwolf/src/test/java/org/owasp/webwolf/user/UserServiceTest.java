package org.owasp.webwolf.user;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private UserService cut;

    @Before
    public void setup(){
        cut = new UserService(mockUserRepository);
    }

    @Test
    public void testLoadUserByUsername(){
        // setup
        final String username = "guest";
        final String password = "123";

        WebGoatUser user = new WebGoatUser(username, password);
        when(mockUserRepository.findByUsername(username)).thenReturn(user);

        // execute
        final WebGoatUser webGoatUser = cut.loadUserByUsername(username);

        // verify
        Assert.assertEquals(username, webGoatUser.getUsername());
        Assert.assertEquals(password, webGoatUser.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_NULL(){
        // setup
        final String username = "guest";
        when(mockUserRepository.findByUsername(username)).thenReturn(null);

        // execute
        cut.loadUserByUsername(username);
    }

    @Test
    public void testAddUser(){
        // setup
        final String username = "guest";
        final String password = "guest";

        // execute
        cut.addUser(username, password);

        // verify
        verify(mockUserRepository, times(1)).save(any(WebGoatUser.class));
    }
}
