package org.owasp.webwolf.user;

import org.assertj.core.api.Assertions;
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

@RunWith(SpringJUnit4ClassRunner.class)
public class UserValidatorTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void validUserFormShouldNotHaveErrors() {
        var validUserForm = new UserForm();
        validUserForm.setUsername("guest");
        validUserForm.setMatchingPassword("123");
        validUserForm.setPassword("123");
        BindException errors = new BindException(validUserForm, "validUserForm");

        userValidator.validate(validUserForm, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void whenPasswordDoNotMatchShouldFail() {
        var validUserForm = new UserForm();
        validUserForm.setUsername("guest");
        validUserForm.setMatchingPassword("123");
        validUserForm.setPassword("124");
        BindException errors = new BindException(validUserForm, "validUserForm");

        userValidator.validate(validUserForm, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void registerExistingUserAgainShouldFail() {
        var username = "guest";
        var password = "123";
        var validUserForm = new UserForm();
        validUserForm.setUsername(username);
        validUserForm.setMatchingPassword(password);
        validUserForm.setPassword("124");
        BindException errors = new BindException(validUserForm, "validUserForm");
        var webGoatUser = new WebGoatUser(username, password);
        when(mockUserRepository.findByUsername(validUserForm.getUsername())).thenReturn(webGoatUser);

        userValidator.validate(validUserForm, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void testSupports() {
        Assertions.assertThat(userValidator.supports(UserForm.class)).isTrue();
    }

    @Test
    public void testSupports_false() {
        Assertions.assertThat(userValidator.supports(UserService.class)).isFalse();
    }
}