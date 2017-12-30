/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 */

package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class HttpBasicsInterceptRequestTest extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        HttpBasicsInterceptRequest httpBasicsInterceptRequest = new HttpBasicsInterceptRequest();
        init(httpBasicsInterceptRequest);
        this.mockMvc = standaloneSetup(httpBasicsInterceptRequest).build();
    }

    @Test
    public void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/1")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("http-proxies.intercept.success"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void failure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("http-proxies.intercept.failure"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}