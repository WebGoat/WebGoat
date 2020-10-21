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

package org.owasp.webgoat.challenges;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.challenges.challenge1.Assignment1;
import org.owasp.webgoat.challenges.challenge1.ImageServlet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.InetAddress;

import static org.mockito.Mockito.when;
import static org.owasp.webgoat.challenges.SolutionConstants.PASSWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author nbaars
 * @since 5/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class Assignment1Test extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        Assignment1 assignment1 = new Assignment1();
        init(assignment1);
        new Flag().initFlags();
        this.mockMvc = standaloneSetup(assignment1).build();
    }

    @Test
    public void success() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress();
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
                .header("X-Forwarded-For", host)
                .param("username", "admin")
                .param("password", SolutionConstants.PASSWORD.replace("1234", String.format("%04d",ImageServlet.PINCODE))))
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("flag: " + Flag.FLAGS.get(1))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void wrongPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
                .param("username", "admin")
                .param("password", "wrong"))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

//    @Test
//    public void correctPasswordXForwardHeaderMissing() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
//                .param("username", "admin")
//                .param("password", SolutionConstants.PASSWORD))
//                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("ip.address.unknown"))))
//                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
//    }

//    @Test
//    public void correctPasswordXForwardHeaderWrong() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/challenge/1")
//                .header("X-Forwarded-For", "127.0.1.2")
//                .param("username", "admin")
//                .param("password", SolutionConstants.PASSWORD))
//                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("ip.address.unknown"))))
//                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
//    }

}