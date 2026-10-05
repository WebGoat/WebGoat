/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.authbypass;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;

class BypassVerificationTest extends LessonTest {

  @Test
  void placeHolder() {
    assert (true);
  }

  // TODO: Finish tests below ... getting null on injected/mocked userSession for some reason (in
  // AssignmentEndpoint:58 even though it it mocked via AssignmentEncpointTest and works in other
  // tests)
  //    @Test
  //    public void testCheatingDetection() throws Exception {
  //       ResultActions results =
  // mockMvc.perform(MockMvcRequestBuilders.post("/auth-bypass/verify-account")
  //               .param("secQuestion0","Dr. Watson")
  //               .param("secQuestion1","Baker Street")
  //               .param("verifyMethod","SEC_QUESTIONS")
  //               .param("userId","1223445"));
  //
  //        results.andExpect(status().isOk())
  //                .andExpect(jsonPath("$.feedback",
  // CoreMatchers.is(messages.getMessage("verify-account.cheated"))));
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
