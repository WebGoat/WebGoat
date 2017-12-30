package org.owasp.webgoat.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void passwordsShouldMatch() {
        UserForm userForm = new UserForm();
        userForm.setAgree("true");
        userForm.setUsername("test1234");
        userForm.setPassword("test1234");
        userForm.setMatchingPassword("test1234");
        Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
        new UserValidator(userRepository).validate(userForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldGiveErrorWhenPasswordsDoNotMatch() {
        UserForm userForm = new UserForm();
        userForm.setAgree("true");
        userForm.setUsername("test1234");
        userForm.setPassword("test12345");
        userForm.setMatchingPassword("test1234");
        Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
        new UserValidator(userRepository).validate(userForm, errors);
        assertTrue(errors.hasErrors());
        assertThat(errors.getFieldError("matchingPassword").getCode()).isEqualTo("password.diff");
    }

    @Test
    public void shouldGiveErrorWhenUserAlreadyExists() {
        UserForm userForm = new UserForm();
        userForm.setAgree("true");
        userForm.setUsername("test12345");
        userForm.setPassword("test12345");
        userForm.setMatchingPassword("test12345");
        when(userRepository.findByUsername(anyString())).thenReturn(new WebGoatUser("test1245", "password"));
        Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
        new UserValidator(userRepository).validate(userForm, errors);
        assertTrue(errors.hasErrors());
        assertThat(errors.getFieldError("username").getCode()).isEqualTo("username.duplicate");
    }

}