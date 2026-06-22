/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return UserForm.class.equals(clazz);
  }

  @Override
  public void validate(Object o, Errors errors) {
    UserForm userForm = (UserForm) o;

    if (userRepository.findByUsername(userForm.getUsername()) != null) {
      errors.rejectValue("username", "username.duplicate");
    }

    if (!userForm.getMatchingPassword().equals(userForm.getPassword())) {
      errors.rejectValue("matchingPassword", "password.diff");
    }
  }
}
