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

package org.owasp.webgoat.xss;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.xss.stored.StoredXssComments;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ExtendWith(MockitoExtension.class)
public class StoredXssCommentsTest extends AssignmentEndpointTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        StoredXssComments storedXssComments = new StoredXssComments();
        init(storedXssComments);
        this.mockMvc = standaloneSetup(storedXssComments).build();
    }

    @Test
    public void success() throws Exception {
        ResultActions results = mockMvc.perform(MockMvcRequestBuilders.post("/CrossSiteScriptingStored/stored-xss")
                .content("{\"text\":\"someTextHere<script>webgoat.customjs.phoneHome()</script>MoreTextHere\"}")
                .contentType(MediaType.APPLICATION_JSON));

        results.andExpect(status().isOk());
        results.andExpect(jsonPath("$.lessonCompleted",CoreMatchers.is(true)));
    }

    @Test
    public void failure() throws Exception {
        ResultActions results = mockMvc.perform(MockMvcRequestBuilders.post("/CrossSiteScriptingStored/stored-xss")
                .content("{\"text\":\"someTextHere<script>alert('Xss')</script>MoreTextHere\"}")
                .contentType(MediaType.APPLICATION_JSON));

        results.andExpect(status().isOk());
        results.andExpect(jsonPath("$.lessonCompleted",CoreMatchers.is(false)));
    }

    /* For the next two tests there is a comment seeded ...
        comments.add(new Comment("secUriTy", DateTime.now().toString(fmt), "<script>console.warn('unit test me')</script>Comment for Unit Testing"));
        ... the isEncoded method will remain commented out as it will fail (because WebGoat isn't supposed to be secure)
     */

    //Ensures it is vulnerable
    @Test
    public void isNotEncoded() throws Exception {
        //do get to get comments after posting xss payload
        ResultActions taintedResults = mockMvc.perform(MockMvcRequestBuilders.get("/CrossSiteScriptingStored/stored-xss"));
        MvcResult mvcResult = taintedResults.andReturn();
        assert(mvcResult.getResponse().getContentAsString().contains("<script>console.warn"));
    }

    //Could be used to test an encoding solution ... commented out so build will pass. Uncommenting will fail build, but leaving in as positive Security Unit Test
//    @Test
//    public void isEncoded() throws Exception {
//        //do get to get comments after posting xss payload
//        ResultActions taintedResults = mockMvc.perform(MockMvcRequestBuilders.get("/CrossSiteScripting/stored-xss"));
//        taintedResults.andExpect(jsonPath("$[0].text",CoreMatchers.is(CoreMatchers.containsString("&lt;scriptgt;"))));
//    }
}
