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

package org.owasp.webgoat.lessons.http_proxies;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.lessons.http_proxies.HttpBasicsInterceptRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class HttpBasicsInterceptRequestTest extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        HttpBasicsInterceptRequest httpBasicsInterceptRequest = new HttpBasicsInterceptRequest();
        init(httpBasicsInterceptRequest);
        this.mockMvc = standaloneSetup(httpBasicsInterceptRequest).build();
    }

    @Test
    public void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.success"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void failure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void missingParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void missingHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void whenPostAssignmentShouldNotPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}
