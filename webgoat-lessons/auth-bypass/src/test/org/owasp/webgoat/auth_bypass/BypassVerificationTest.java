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

package org.owasp.webgoat.auth_bypass;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class BypassVerificationTest extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        VerifyAccount verifyAccount = new VerifyAccount();
        init(verifyAccount);
        this.mockMvc = standaloneSetup(verifyAccount).build();
    }

    @Test
    public void placeHolder() {
        assert (true);
    }

//TODO: Finish tests below ... getting null on injected/mocked userSession for some reason (in AssignmentEndpoint:58 even though it it mocked via AssignmentEncpointTest and works in other tests)
//    @Test
//    public void testCheatingDetection() throws Exception {
//       ResultActions results = mockMvc.perform(MockMvcRequestBuilders.post("/auth-bypass/verify-account")
//               .param("secQuestion0","Dr. Watson")
//               .param("secQuestion1","Baker Street")
//               .param("verifyMethod","SEC_QUESTIONS")
//               .param("userId","1223445"));
//
//        results.andExpect(status().isOk())
//                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("verify-account.cheated"))));
//    }

//    @Test
//    public void success() {
//
//    }

//    @Test
//    public void failure() {
//
//    }

}
