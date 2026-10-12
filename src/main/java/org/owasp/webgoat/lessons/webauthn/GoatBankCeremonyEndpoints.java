/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.RP_NAME;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.TOM;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.base64Url;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.StoredCredential;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.RelyingParty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Supporting endpoints for the GoatBank assignments: the relying party's "options" endpoints, a
 * simulated security key for Tom and the leaked support ticket.
 */
@RestController
@RequiredArgsConstructor
public class GoatBankCeremonyEndpoints {

  static final String REGISTRATION_CHALLENGE = "webauthn.goatbank.registration.challenge";
  static final String AUTHENTICATION_CHALLENGE = "webauthn.goatbank.authentication.challenge";

  private final WebAuthnLessonState lessonState;

  /** Tom is signed in to GoatBank and adds a new security key to his account. */
  @PostMapping(value = "/WebAuthn/registration/options", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> registrationOptions(
      @CurrentUsername String username, HttpServletRequest request, HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String challenge = WebAuthnSupport.randomChallenge();
    session.setAttribute(REGISTRATION_CHALLENGE, challenge);

    Map<String, Object> options = new LinkedHashMap<>();
    options.put("challenge", challenge);
    options.put("rp", Map.of("id", rp.id(), "name", rp.name()));
    options.put(
        "user",
        Map.of(
            "id", base64Url(WebAuthnLessonState.userHandle(TOM)),
            "name", TOM,
            "displayName", "Tom Cat"));
    options.put("pubKeyCredParams", List.of(Map.of("type", "public-key", "alg", -7)));
    options.put("timeout", 60000);
    options.put("attestation", "none");
    options.put(
        "excludeCredentials",
        descriptors(lessonState.forUser(username).bankCredentialsOf(TOM)));
    return options;
  }

  @PostMapping(
      value = "/WebAuthn/authentication/options",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> authenticationOptions(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> body,
      HttpServletRequest request,
      HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String challenge = WebAuthnSupport.randomChallenge();
    session.setAttribute(AUTHENTICATION_CHALLENGE, challenge);
    String account = String.valueOf(body.getOrDefault("username", ""));

    Map<String, Object> options = new LinkedHashMap<>();
    options.put("challenge", challenge);
    options.put("rpId", rp.id());
    options.put(
        "allowCredentials", descriptors(lessonState.forUser(username).bankCredentialsOf(account)));
    options.put("userVerification", "discouraged");
    options.put("timeout", 60000);
    return options;
  }

  /** Simulates the browser calling Tom's security key with {@code navigator.credentials.create}. */
  @PostMapping(
      value = "/WebAuthn/authenticator/create",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> simulateCreate(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> options,
      HttpServletRequest request) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String rpId = WebAuthnSupport.path(options, "rp", "id");
    if (!rp.id().equals(rpId)) {
      return error("SecurityError: rp.id is not valid for origin " + rp.origin());
    }
    try {
      byte[] userHandle = WebAuthnSupport.fromBase64Url(WebAuthnSupport.path(options, "user", "id"));
      String challenge = WebAuthnSupport.path(options, "challenge");
      return ResponseEntity.ok(
          lessonState
              .forUser(username)
              .tomsSecurityKey()
              .create(rp.origin(), rpId, challenge, userHandle));
    } catch (IllegalArgumentException e) {
      return error("TypeError: " + e.getMessage());
    }
  }

  /** Simulates the browser calling Tom's security key with {@code navigator.credentials.get}. */
  @PostMapping(
      value = "/WebAuthn/authenticator/get",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> simulateGet(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> options,
      HttpServletRequest request) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String rpId = WebAuthnSupport.path(options, "rpId");
    if (!rp.id().equals(rpId)) {
      return error("SecurityError: rpId is not valid for origin " + rp.origin());
    }
    List<String> allowed =
        options.get("allowCredentials") instanceof List<?> list
            ? list.stream()
                .filter(Map.class::isInstance)
                .map(d -> String.valueOf(((Map<?, ?>) d).get("id")))
                .toList()
            : List.of();
    try {
      return ResponseEntity.ok(
          lessonState
              .forUser(username)
              .tomsSecurityKey()
              .get(rp.origin(), rpId, WebAuthnSupport.path(options, "challenge"), allowed));
    } catch (IllegalStateException e) {
      return error(e.getMessage());
    }
  }

  /** Attachment of support ticket #4711: a request captured while debugging Jerry's login. */
  @GetMapping(value = "/WebAuthn/support-ticket", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> supportTicket(HttpServletRequest request) {
    return lessonState.capturedLoginOfJerry(RelyingParty.from(request, RP_NAME));
  }

  private static List<Map<String, Object>> descriptors(List<StoredCredential> credentials) {
    return credentials.stream()
        .map(c -> Map.<String, Object>of("type", "public-key", "id", c.credentialId()))
        .toList();
  }

  private static ResponseEntity<Map<String, Object>> error(String message) {
    return ResponseEntity.badRequest().body(Map.of("error", message));
  }
}
