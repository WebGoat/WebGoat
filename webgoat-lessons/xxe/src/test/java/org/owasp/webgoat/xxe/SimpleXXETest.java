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

package org.owasp.webgoat.xxe;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.xxe.XXE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleXXETest extends LessonTest {

    @Autowired
    private XXE xxe;

    @Before
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(xxe);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void workingAttack() throws Exception {
        //Call with XXE injection
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
    }

    @Test
    public void postingJsonCommentShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<comment><text>test</ext></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

    @Test
    public void postingXmlCommentWithoutXXEShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

    @Test
    public void postingPlainTextShouldShwoException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/simple")
                .content("test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.startsWith("javax.xml.bind.UnmarshalException\\n - with linked exception")))
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
    }

}