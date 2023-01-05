package org.owasp.webgoat.container.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

  @Mock private UserRepository userRepository;

  @Test
  void passwordsShouldMatch() {
    UserForm userForm = new UserForm();
    userForm.setAgree("true");
    userForm.setUsername("test1234");
    userForm.setPassword("test1234");
    userForm.setMatchingPassword("test1234");
    Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
    new UserValidator(userRepository).validate(userForm, errors);
    Assertions.assertThat(errors.hasErrors()).isFalse();
  }

  @Test
  void shouldGiveErrorWhenPasswordsDoNotMatch() {
    UserForm userForm = new UserForm();
    userForm.setAgree("true");
    userForm.setUsername("test1234");
    userForm.setPassword("test12345");
    userForm.setMatchingPassword("test1234");
    Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
    new UserValidator(userRepository).validate(userForm, errors);
    Assertions.assertThat(errors.hasErrors()).isTrue();
    assertThat(errors.getFieldError("matchingPassword").getCode()).isEqualTo("password.diff");
  }

  @Test
  void shouldGiveErrorWhenUserAlreadyExists() {
    UserForm userForm = new UserForm();
    userForm.setAgree("true");
    userForm.setUsername("test12345");
    userForm.setPassword("test12345");
    userForm.setMatchingPassword("test12345");
    when(userRepository.findByUsername(anyString()))
        .thenReturn(new WebGoatUser("test1245", "password"));
    Errors errors = new BeanPropertyBindingResult(userForm, "userForm");
    new UserValidator(userRepository).validate(userForm, errors);
    Assertions.assertThat(errors.hasErrors()).isTrue();
    assertThat(errors.getFieldError("username").getCode()).isEqualTo("username.duplicate");
  }
}
