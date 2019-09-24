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

package org.owasp.webgoat.missing_ac;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class MissingFunctionACUsersTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @Before
    public void setup() {
        MissingFunctionACUsers usersController = new MissingFunctionACUsers();
        this.mockMvc = standaloneSetup(usersController).build();
        ReflectionTestUtils.setField(usersController,"userService",userService);
        when(userService.getAllUsers()).thenReturn(getUsersList());
    }

    @Test
    public void TestContentTypeApplicationJSON () throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Content-type","application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", CoreMatchers.is("user1")))
                .andExpect(jsonPath("$[0].userHash",CoreMatchers.is("cplTjehjI/e5ajqTxWaXhU5NW9UotJfXj+gcbPvfWWc=")))
                .andExpect(jsonPath("$[1].admin",CoreMatchers.is(true)));

    }

    private List<WebGoatUser> getUsersList() {
        List <WebGoatUser> tempUsers = new ArrayList<>();
        tempUsers.add(new WebGoatUser("user1","password1"));
        tempUsers.add(new WebGoatUser("user2","password2","WEBGOAT_ADMIN"));
        tempUsers.add(new WebGoatUser("user3","password3", "WEBGOAT_USER"));
        return tempUsers;
    }



}
