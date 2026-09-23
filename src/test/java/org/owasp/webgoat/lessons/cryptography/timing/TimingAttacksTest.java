/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.container.session.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

class TimingAttacksTest extends LessonTest {

  @Autowired private Course course;

  @Test
  void assignmentsAreAttachedToTheTimingLesson() {
    var timingLesson = course.getLessonByName(new LessonName("TimingAttacks"));

    assertThat(timingLesson.getAssignments())
        .extracting(assignment -> assignment.getName())
        .containsExactlyInAnyOrder(
            "TimingAttackFirstByteAssignment",
            "TimingAttackTagAssignment",
            "TimingAttackNoiseAssignment",
            "TimingAttackMitigationAssignment");
  }

  @Test
  void lessonPageRenders() throws Exception {
    mockMvc
        .perform(get("/TimingAttacks.lesson"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Discover the side channel")))
        .andExpect(content().string(containsString("MessageDigest.isEqual")))
        .andExpect(content().string(containsString("Connection to JWT")));
  }

  @Test
  void invalidOracleResponsesAreIndistinguishable() throws Exception {
    MockHttpSession session = new MockHttpSession();
    TimingAttackSessionState state = initializeState(session);
    String incorrectTag = incorrectTagFor(state, TimingAttackSessionState.KNOWN_MESSAGE);

    mockMvc
        .perform(
            get("/crypto/timing/verify")
                .session(session)
                .param("message", TimingAttackSessionState.KNOWN_MESSAGE)
                .param("signature", "not-hex!"))
        .andExpect(status().isOk())
        .andExpect(content().string(TimingAttackOracle.INVALID));

    mockMvc
        .perform(
            get("/crypto/timing/verify")
                .session(session)
                .param("message", TimingAttackSessionState.KNOWN_MESSAGE)
                .param("signature", incorrectTag))
        .andExpect(status().isOk())
        .andExpect(content().string(TimingAttackOracle.INVALID));
  }

  @Test
  void oracleAcceptsTheDynamicSessionTag() throws Exception {
    MockHttpSession session = new MockHttpSession();
    String tag = tagFor(initializeState(session), TimingAttackSessionState.KNOWN_MESSAGE);
    mockMvc
        .perform(
            get("/crypto/timing/verify-tag")
                .session(session)
                .param("message", TimingAttackSessionState.KNOWN_MESSAGE)
                .param("signature", tag))
        .andExpect(status().isOk())
        .andExpect(content().string(TimingAttackOracle.VALID));
  }

  @Test
  void firstByteAssignmentChecksTheCurrentSession() throws Exception {
    MockHttpSession session = new MockHttpSession();
    String tag = tagFor(initializeState(session), TimingAttackSessionState.KNOWN_MESSAGE);

    mockMvc
        .perform(post("/crypto/timing/first-byte").session(session).param("firstByte", "zz"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));

    mockMvc
        .perform(
            post("/crypto/timing/first-byte")
                .session(session)
                .param("firstByte", tag.substring(0, 2)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void completeTagAssignmentsUseTheCorrectPerSessionTags() throws Exception {
    MockHttpSession session = new MockHttpSession();
    TimingAttackSessionState state = initializeState(session);
    String tag = tagFor(state, TimingAttackSessionState.KNOWN_MESSAGE);
    String noisyTag = tagFor(state, TimingAttackSessionState.NOISY_MESSAGE);

    mockMvc
        .perform(post("/crypto/timing/tag").session(session).param("signature", tag))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));

    mockMvc
        .perform(post("/crypto/timing/noisy-tag").session(session).param("signature", noisyTag))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void differentSessionsHaveIndependentState() throws Exception {
    TimingAttackSessionState first = initializeState(new MockHttpSession());
    TimingAttackSessionState second = initializeState(new MockHttpSession());

    assertThat(first).isNotSameAs(second);
  }

  @Test
  void oracleLimitsConcurrentRequestsForOneSession() throws Exception {
    MockHttpSession session = new MockHttpSession();
    TimingAttackSessionState state = initializeState(session);
    for (int i = 0; i < TimingAttackSessionState.MAX_CONCURRENT_REQUESTS; i++) {
      assertThat(state.tryAcquireRequestSlot()).isTrue();
    }

    try {
      mockMvc
          .perform(get("/crypto/timing/verify").session(session).param("signature", "00000000"))
          .andExpect(status().isTooManyRequests());
    } finally {
      for (int i = 0; i < TimingAttackSessionState.MAX_CONCURRENT_REQUESTS; i++) {
        state.releaseRequestSlot();
      }
    }
  }

  @Test
  void mitigationRequiresTheRootCauseAndAppropriateApi() throws Exception {
    mockMvc
        .perform(
            post("/crypto/timing/mitigation")
                .param("cause", "early-exit")
                .param("mitigation", "random-delay"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false))
        .andExpect(jsonPath("$.feedback").value(containsString("secret-dependent early exit")));

    mockMvc
        .perform(
            post("/crypto/timing/mitigation")
                .param("cause", "early-exit")
                .param("mitigation", "message-digest"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  private TimingAttackSessionState initializeState(MockHttpSession session) throws Exception {
    mockMvc
        .perform(get("/crypto/timing/verify").session(session))
        .andExpect(status().isOk())
        .andExpect(content().string(TimingAttackOracle.INVALID));

    return Collections.list(session.getAttributeNames()).stream()
        .map(session::getAttribute)
        .filter(TimingAttackSessionState.class::isInstance)
        .map(TimingAttackSessionState.class::cast)
        .findFirst()
        .orElseThrow();
  }

  private static String tagFor(TimingAttackSessionState state, String message) {
    return HexFormat.of().formatHex(state.tagFor(message));
  }

  private static String incorrectTagFor(TimingAttackSessionState state, String message) {
    byte[] tag = state.tagFor(message);
    tag[0] ^= 1;
    return HexFormat.of().formatHex(tag);
  }
}
