/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository mockUserRepository;

  @InjectMocks private UserService sut;

  @Test
  public void testLoadUserByUsername() {
    var username = "guest";
    var password = "123";
    WebWolfUser user = new WebWolfUser(username, password);
    when(mockUserRepository.findByUsername(username)).thenReturn(user);

    var webGoatUser = sut.loadUserByUsername(username);

    Assertions.assertThat(username).isEqualTo(webGoatUser.getUsername());
    Assertions.assertThat(password).isEqualTo(webGoatUser.getPassword());
  }

  @Test
  public void testLoadUserByUsername_NULL() {
    var username = "guest";

    when(mockUserRepository.findByUsername(username)).thenReturn(null);

    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> sut.loadUserByUsername(username));
  }

  @Test
  public void testAddUser() {
    var username = "guest";
    var password = "guest";

    sut.addUser(username, password);

    verify(mockUserRepository, times(1)).save(any(WebWolfUser.class));
  }
}
