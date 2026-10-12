/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;

class WebAuthnLessonTest extends LessonTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  // MockMvc requests use http://localhost
  private static final String ORIGIN = "http://localhost";
  private static final String RP_ID = "localhost";

  private MockHttpSession session;

  @BeforeEach
  void setup() {
    session = new MockHttpSession();
  }

  @Test
  void registeringOwnKeyOnJerrysAccountSolvesAssignment() throws Exception {
    Map<String, Object> options = postJson("/WebAuthn/registration/options", Map.of());
    Map<String, Object> credential = postJson("/WebAuthn/authenticator/create", options);

    finishRegistration(WebAuthnSupport.base64Url("jerry".getBytes(StandardCharsets.UTF_8)), credential)
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  void honestRegistrationDoesNotSolveAssignment() throws Exception {
    Map<String, Object> options = postJson("/WebAuthn/registration/options", Map.of());
    Map<String, Object> credential = postJson("/WebAuthn/authenticator/create", options);

    finishRegistration(userId(options), credential)
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("registered for account tom")));
  }

  @Test
  void registrationWithForeignChallengeIsRejected() throws Exception {
    postJson("/WebAuthn/registration/options", Map.of());
    var credential =
        new VirtualAuthenticator()
            .create(ORIGIN, RP_ID, WebAuthnSupport.randomChallenge(), new byte[] {1});

    finishRegistration(WebAuthnSupport.base64Url("jerry".getBytes(StandardCharsets.UTF_8)), credential)
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("challenge does not match")));
  }

  @Test
  void registrationFromOtherOriginIsRejected() throws Exception {
    Map<String, Object> options = postJson("/WebAuthn/registration/options", Map.of());
    var credential =
        new VirtualAuthenticator()
            .create("https://goatbank.evil", RP_ID, (String) options.get("challenge"), new byte[] {1});

    finishRegistration(userId(options), credential)
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("unexpected origin")));
  }

  @Test
  void tomCanSignInWithHisOwnKey() throws Exception {
    Map<String, Object> options = postJson("/WebAuthn/authentication/options", Map.of("username", "tom"));
    Map<String, Object> credential = postJson("/WebAuthn/authenticator/get", options);

    finishAuthentication(Map.of("username", "tom", "credential", credential))
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("Signed in as tom")));
  }

  @Test
  void tomsKeyCannotSignForJerry() throws Exception {
    Map<String, Object> options =
        postJson("/WebAuthn/authentication/options", Map.of("username", "jerry"));

    mockMvc
        .perform(
            post("/WebAuthn/authenticator/get")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(options)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", containsString("NotAllowedError")));
  }

  @Test
  void replayingCapturedLoginSolvesAssignment() throws Exception {
    String captured =
        mockMvc
            .perform(get("/WebAuthn/support-ticket").session(session))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    postJson("/WebAuthn/authentication/options", Map.of("username", "jerry"));

    finishAuthentication(MAPPER.readValue(captured, Map.class))
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  @SuppressWarnings("unchecked")
  void capturedCredentialCannotBeUsedForAnotherAccount() throws Exception {
    Map<String, Object> captured =
        MAPPER.readValue(
            mockMvc
                .perform(get("/WebAuthn/support-ticket").session(session))
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Map.class);

    finishAuthentication(Map.of("username", "tom", "credential", captured.get("credential")))
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("unknown credential")));
  }

  @Test
  void securityKeyRegistrationAndSignInSolvesBonus() throws Exception {
    var securityKey = new VirtualAuthenticator();
    Map<String, Object> registrationOptions =
        postJson("/WebAuthn/securitykey/registration/options", Map.of());
    var credential =
        securityKey.create(
            ORIGIN,
            RP_ID,
            (String) registrationOptions.get("challenge"),
            WebAuthnSupport.fromBase64Url(userId(registrationOptions)));
    Map<String, Object> registration =
        postJson("/WebAuthn/securitykey/registration/finish", credential);
    assertThat(registration.get("registered")).isEqualTo(true);

    var assertion = signIn(securityKey);
    finishSecurityKey(assertion).andExpect(jsonPath("$.lessonCompleted", is(true)));

    // the challenge is single use: replaying the same assertion fails
    postJson("/WebAuthn/securitykey/authentication/options", Map.of());
    finishSecurityKey(assertion)
        .andExpect(jsonPath("$.lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", containsString("challenge mismatch")));
  }

  @Test
  void securityKeyRegistrationRejectsReplayedChallenge() throws Exception {
    var securityKey = new VirtualAuthenticator();
    Map<String, Object> options = postJson("/WebAuthn/securitykey/registration/options", Map.of());
    var credential =
        securityKey.create(
            ORIGIN, RP_ID, (String) options.get("challenge"), WebAuthnSupport.randomBytes(32));
    postJson("/WebAuthn/securitykey/registration/finish", credential);

    mockMvc
        .perform(
            post("/WebAuthn/securitykey/registration/finish")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(credential)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", containsString("No registration in progress")));
  }

  @Test
  void quizIsSolvedWithCorrectAnswers() throws Exception {
    mockMvc
        .perform(
            post("/WebAuthn/quiz")
                .param("question_0_solution", "Solution 2: User enumeration")
                .param("question_1_solution", "Solution 2")
                .param("question_2_solution", "Solution 2")
                .param("question_3_solution", "Solution 2")
                .param("question_4_solution", "Solution 1"))
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  private Map<String, Object> signIn(VirtualAuthenticator securityKey) throws Exception {
    Map<String, Object> options = postJson("/WebAuthn/securitykey/authentication/options", Map.of());
    List<String> allowed =
        ((List<?>) options.get("allowCredentials"))
            .stream().map(d -> (String) ((Map<?, ?>) d).get("id")).toList();
    return securityKey.get(ORIGIN, RP_ID, (String) options.get("challenge"), allowed);
  }

  private static String userId(Map<String, Object> options) {
    return (String) ((Map<?, ?>) options.get("user")).get("id");
  }

  private ResultActions finishRegistration(String userHandle, Map<String, Object> credential)
      throws Exception {
    return mockMvc
        .perform(
            post("/WebAuthn/registration/finish")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    MAPPER.writeValueAsString(
                        Map.of("userHandle", userHandle, "credential", credential))))
        .andExpect(status().isOk());
  }

  private ResultActions finishAuthentication(Map<?, ?> body) throws Exception {
    return mockMvc
        .perform(
            post("/WebAuthn/authentication/finish")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(body)))
        .andExpect(status().isOk());
  }

  private ResultActions finishSecurityKey(Map<String, Object> assertion) throws Exception {
    return mockMvc
        .perform(
            post("/WebAuthn/securitykey/authentication/finish")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(assertion)))
        .andExpect(status().isOk());
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> postJson(String url, Object body) throws Exception {
    String response =
        mockMvc
            .perform(
                post(url)
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return MAPPER.readValue(response, Map.class);
  }
}
