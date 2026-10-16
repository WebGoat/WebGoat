/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.container.session.Course;
import org.springframework.beans.factory.annotation.Autowired;

class Challenge2Test extends LessonTest {

  private static final URI IMPORT_URI =
      URI.create("http://localhost:8080/WebGoat/northstar/import");

  @Autowired private Course course;

  @Test
  void challengeHasOneAssignmentAndRendersTheDashboard() throws Exception {
    var lesson = course.getLessonByName(new LessonName("Challenge2"));
    assertThat(lesson.getPackage()).isEqualTo("challenges");
    assertThat(lesson.getTitle()).isEqualTo("challenge2.title");
    assertThat(lesson.getAssignments())
        .extracting(Assignment::getName)
        .containsExactly("Assignment2");

    mockMvc
        .perform(get("/Challenge2.lesson.lesson"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("<h3>Partner feed recovery</h3>")))
        .andExpect(content().string(containsString("Import a partner feed")))
        .andExpect(content().string(containsString("/northstar/feeds")))
        .andExpect(content().string(containsString("/northstar/import")))
        .andExpect(content().string(containsString("/northstar/recovery")))
        .andExpect(content().string(containsString("fa-flag-checkered")))
        .andExpect(content().string(containsString("Submit result")));
  }

  @Test
  void directDiagnosticRequestAndForgedHeaderAreUnauthorized() throws Exception {
    mockMvc
        .perform(get("/northstar/internal/diagnostics"))
        .andExpect(status().isUnauthorized());
    mockMvc
        .perform(
            get("/northstar/internal/diagnostics")
                .header(InternalDiagnostic.INTERNAL_HEADER, "true"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void publisherLinkIsAnOpenRedirect() throws Exception {
    mockMvc
        .perform(get("/northstar/go").param("url", "https://example.org"))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "https://example.org"));
  }

  @Test
  void partnerBulletinLinkOpensInWebGoat() throws Exception {
    mockMvc
        .perform(get("/northstar/feeds"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Partner bulletin:")));
  }

  @Test
  void ordinaryFeedImportReturnsAPreview() throws Exception {
    mockMvc
        .perform(
            post("/northstar/import")
                .param("xml", "<feed><story>Evening bulletin</story></feed>"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preview").value("Evening bulletin"));
  }

  @Test
  void xmlEntityMustPassThroughThePublisherRedirect() {
    Challenge2State state = new Challenge2State();
    ChainedFetchService fetcher = fetcher(state);
    ChainedXmlService xmlService = new ChainedXmlService(fetcher);
    String internal = IMPORT_URI.resolve("internal/diagnostics").toString();
    String redirect = redirectTo(internal);

    assertThatThrownBy(() -> fetcher.fetch(internal, state, IMPORT_URI))
        .isInstanceOf(IllegalArgumentException.class);
    assertThat(fetcher.fetch(redirect, state, IMPORT_URI).body()).isEqualTo(state.flag());
    assertThat(state.completedXmlChain()).isFalse();

    String xml = entityFeed(redirect);
    assertThat(xmlService.parse(xml, state, IMPORT_URI)).isEqualTo(state.flag());
    assertThat(state.completedXmlChain()).isTrue();
  }

  @Test
  void relativeEntityAndRedirectTargetsWorkBehindAnyContextPath() {
    Challenge2State state = new Challenge2State();
    ChainedXmlService xmlService = new ChainedXmlService(fetcher(state));

    assertThat(xmlService.parse(entityFeed("go?url=internal%2Fdiagnostics"), state, IMPORT_URI))
        .isEqualTo(state.flag());
    assertThat(state.completedXmlChain()).isTrue();
  }

  @Test
  void parserRejectsFilesAndArbitraryHostsWithoutMarkingTheChallenge() {
    Challenge2State state = new Challenge2State();
    ChainedXmlService xmlService = new ChainedXmlService(fetcher(state));

    assertThatThrownBy(() -> xmlService.parse(entityFeed("file:///etc/passwd"), state, IMPORT_URI))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> xmlService.parse(entityFeed("http://example.org/"), state, IMPORT_URI))
        .isInstanceOf(IllegalArgumentException.class);
    assertThat(state.completedXmlChain()).isFalse();
  }

  @Test
  void malformedXmlDoesNotMarkTheChallengeEvenAfterTheInternalFetch() {
    Challenge2State state = new Challenge2State();
    ChainedXmlService xmlService = new ChainedXmlService(fetcher(state));
    String xml =
        "<!DOCTYPE feed [<!ENTITY flag SYSTEM \""
            + redirectTo(IMPORT_URI.resolve("internal/diagnostics").toString())
            + "\">]><feed>&flag;";

    assertThatThrownBy(() -> xmlService.parse(xml, state, IMPORT_URI))
        .isInstanceOf(IllegalArgumentException.class);
    assertThat(state.completedXmlChain()).isFalse();
  }

  @Test
  void redirectDepthIsBounded() {
    Challenge2State state = new Challenge2State();
    String url = IMPORT_URI.resolve("feeds").toString();
    for (int i = 0; i < 5; i++) {
      url = redirectTo(url);
    }
    String nestedRedirect = url;

    assertThatThrownBy(() -> fetcher(state).fetch(nestedRedirect, state, IMPORT_URI))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Too many redirects");
  }

  @Test
  void sessionFlagsAndInternalTokensAreIndependent() {
    Challenge2State first = new Challenge2State();
    Challenge2State second = new Challenge2State();

    assertThat(first.flag()).matches("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
    assertThat(first.flag()).isNotEqualTo(second.flag());
    assertThat(first.internalToken()).isNotEqualTo(second.internalToken());
    assertThat(new InternalDiagnostic(second).diagnostics(first.internalToken()).getStatusCode().value())
        .isEqualTo(401);
  }

  private ChainedFetchService fetcher(Challenge2State state) {
    return new ChainedFetchService(new InternalDiagnostic(state), new NorthstarEndpoints());
  }

  private String redirectTo(String destination) {
    return IMPORT_URI.resolve(
            "go?url=" + URLEncoder.encode(destination, StandardCharsets.UTF_8))
        .toString();
  }

  private String entityFeed(String url) {
    return "<!DOCTYPE feed [<!ENTITY flag SYSTEM \"" + url + "\">]><feed>&flag;</feed>";
  }
}
