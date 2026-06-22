/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xss;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class StoredXssCommentsTest extends LessonTest {

  @Test
  void success() throws Exception {
    ResultActions results =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/CrossSiteScriptingStored/stored-xss")
                .content(
                    "{\"text\":\"someTextHere<script>webgoat.customjs.phoneHome()</script>MoreTextHere\"}")
                .contentType(MediaType.APPLICATION_JSON));

    results.andExpect(status().isOk());
    results.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void failure() throws Exception {
    ResultActions results =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/CrossSiteScriptingStored/stored-xss")
                .content("{\"text\":\"someTextHere<script>alert('Xss')</script>MoreTextHere\"}")
                .contentType(MediaType.APPLICATION_JSON));

    results.andExpect(status().isOk());
    results.andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  /* For the next two tests there is a comment seeded ...
     comments.add(new Comment("secUriTy", DateTime.now().toString(fmt), "<script>console.warn('unit test me')</script>Comment for Unit Testing"));
     ... the isEncoded method will remain commented out as it will fail (because WebGoat isn't supposed to be secure)
  */

  // Ensures it is vulnerable
  @Test
  public void isNotEncoded() throws Exception {
    // do get to get comments after posting xss payload
    ResultActions taintedResults =
        mockMvc.perform(MockMvcRequestBuilders.get("/CrossSiteScriptingStored/stored-xss"));
    MvcResult mvcResult = taintedResults.andReturn();
    assert (mvcResult.getResponse().getContentAsString().contains("<script>console.warn"));
  }

  // Could be used to test an encoding solution ... commented out so build will pass. Uncommenting
  // will fail build, but leaving in as positive Security Unit Test
  //    @Test
  //    public void isEncoded() throws Exception {
  //        //do get to get comments after posting xss payload
  //        ResultActions taintedResults =
  // mockMvc.perform(MockMvcRequestBuilders.get("/CrossSiteScripting/stored-xss"));
  //
  // taintedResults.andExpect(jsonPath("$[0].text",CoreMatchers.is(CoreMatchers.containsString("&lt;scriptgt;"))));
  //    }
}
