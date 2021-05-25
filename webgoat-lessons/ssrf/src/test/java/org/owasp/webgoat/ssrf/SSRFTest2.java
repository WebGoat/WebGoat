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

package org.owasp.webgoat.ssrf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author afry 
 * @since 12/28/18.
 */
@ExtendWith(SpringExtension.class)
public class SSRFTest2 extends LessonTest {

    @Autowired
    private SSRF ssrf;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(ssrf);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void modifyUrlIfconfigPro() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SSRF/task2")
                .param("url", "http://ifconfig.pro"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void modifyUrlCat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SSRF/task2")
                .param("url", "images/cat.jpg"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }
}
