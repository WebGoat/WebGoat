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

package org.owasp.webgoat.csrf;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@ExtendWith(SpringExtension.class)
public class CSRFFeedbackTest extends LessonTest {

    @Autowired
    private CSRF csrf;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(csrf);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void postingJsonMessageThroughWebGoatShouldWork() throws Exception {
        mockMvc.perform(post("/csrf/feedback/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\": \"service\", \"message\":\"dsaffd\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void csrfAttack() throws Exception {
        mockMvc.perform(post("/csrf/feedback/message")
                .contentType(MediaType.TEXT_PLAIN)
                .cookie(new Cookie("JSESSIONID", "test"))
                .header("host", "localhost:8080")
                .header("referer", "webgoat.org")
                .content("{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\": \"service\", \"message\":\"dsaffd\"}"))
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("feedback", StringContains.containsString("the flag is: ")));
    }
}