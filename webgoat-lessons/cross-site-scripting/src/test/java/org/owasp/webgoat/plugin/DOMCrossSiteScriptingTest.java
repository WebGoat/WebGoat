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
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(MockitoJUnitRunner.class)
public class DOMCrossSiteScriptingTest extends AssignmentEndpointTest {
    private MockMvc mockMvc;
    private UserSessionData mockUserSessionData;
    private String randVal =  "12034837";

    @Before
    public void setup() {
        DOMCrossSiteScripting domXss = new DOMCrossSiteScripting();
        init(domXss);
        this.mockMvc = standaloneSetup(domXss).build();
        // mocks
        when(userSessionData.getValue("randValue")).thenReturn(randVal);
    }

    @Test
    public void success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by","dom-xss-vuln")
                .param("param1", "42")
                .param("param2", "24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.containsString("phoneHome Response is " + randVal)))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void failure() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by","wrong-value")
                .param("param1", "22")
                .param("param2", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

}
