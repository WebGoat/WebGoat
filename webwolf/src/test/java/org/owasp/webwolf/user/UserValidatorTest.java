/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webwolf.user;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;

@ExtendWith(MockitoExtension.class)
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