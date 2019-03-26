package org.owasp.webwolf.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

/**
 * @author rjclancy
 * @since 3/26/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserValidatorTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void testValidUserForm() {
        UserForm validUserForm = new UserForm();
        validUserForm.setUsername("guest");
        validUserForm.setMatchingPassword("123");
        validUserForm.setPassword("123");
        BindException errors = new BindException(validUserForm, "validUserForm");

        userValidator.validate(validUserForm, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidUserForm_INVALID_PASSWORD() {
        UserForm validUserForm = new UserForm();
        validUserForm.setUsername("guest");
        validUserForm.setMatchingPassword("123");
        validUserForm.setPassword("124");
        BindException errors = new BindException(validUserForm, "validUserForm");

        userValidator.validate(validUserForm, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidUserForm_DUPLICATE_USER() {
        // setup
        final String username = "guest";
        final String password = "123";

        UserForm validUserForm = new UserForm();
        validUserForm.setUsername(username);
        validUserForm.setMatchingPassword(password);
        validUserForm.setPassword("124");
        BindException errors = new BindException(validUserForm, "validUserForm");

        WebGoatUser webGoatUser = new WebGoatUser(username, password);
        when(mockUserRepository.findByUsername(validUserForm.getUsername())).thenReturn(webGoatUser);

        // execute
        userValidator.validate(validUserForm, errors);

        // verify
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testSupports(){
        Assert.assertTrue(userValidator.supports(UserForm.class));
    }

    @Test
    public void testSupports_false(){
        Assert.assertFalse(userValidator.supports(UserService.class));
    }
}