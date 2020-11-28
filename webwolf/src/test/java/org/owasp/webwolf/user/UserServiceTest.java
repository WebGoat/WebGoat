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

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void testLoadUserByUsername_NULL(){
        var username = "guest";
        
        when(mockUserRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, ()->sut.loadUserByUsername(username));
    }

    @Test
    public void testAddUser(){
        var username = "guest";
        var password = "guest";

        sut.addUser(username, password);

        verify(mockUserRepository, times(1)).save(any(WebGoatUser.class));
    }
}
