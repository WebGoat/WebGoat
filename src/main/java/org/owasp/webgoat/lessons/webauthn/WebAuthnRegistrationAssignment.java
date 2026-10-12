/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.informationMessage;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.owasp.webgoat.lessons.webauthn.GoatBankCeremonyEndpoints.REGISTRATION_CHALLENGE;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.JERRY;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.RP_NAME;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.path;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.StoredCredential;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.UserState;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.AttestationObject;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.ClientData;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.RelyingParty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * GoatBank's registration endpoint. All checks on the authenticator response are in place, but
 * the account the new credential is bound to is taken from the request body.
 */
@RestController
@RequiredArgsConstructor
@AssignmentHints({
  "webauthn.registration.hint1",
  "webauthn.registration.hint2",
  "webauthn.registration.hint3",
  "webauthn.registration.hint4"
})
public class WebAuthnRegistrationAssignment implements AssignmentEndpoint {

  private final WebAuthnLessonState lessonState;

  @PostMapping(
      value = "/WebAuthn/registration/finish",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult finishRegistration(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> body,
      HttpServletRequest request,
      HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String expectedChallenge = (String) session.getAttribute(REGISTRATION_CHALLENGE);
    session.removeAttribute(REGISTRATION_CHALLENGE);
    if (expectedChallenge == null) {
      return failed(this).feedback("webauthn.registration.no-challenge").build();
    }
    UserState state = lessonState.forUser(username);

    try {
      ClientData clientData =
          WebAuthnSupport.parseClientData(path(body, "credential", "response", "clientDataJSON"));
      if (!"webauthn.create".equals(clientData.type())) {
        return rejected("clientDataJSON.type must be 'webauthn.create'");
      }
      if (!WebAuthnSupport.sameBase64Url(expectedChallenge, clientData.challenge())) {
        return rejected("challenge does not match the challenge issued for this session");
      }
      if (!rp.origin().equals(clientData.origin())) {
        return rejected("unexpected origin " + clientData.origin());
      }

      AttestationObject attestation =
          WebAuthnSupport.parseAttestationObject(
              path(body, "credential", "response", "attestationObject"));
      var authData = attestation.authData();
      if (!MessageDigest.isEqual(rp.idHash(), authData.rpIdHash())) {
        return rejected("rpIdHash does not match " + rp.id());
      }
      if (!authData.userPresent()) {
        return rejected("user presence flag not set");
      }
      if (!authData.hasAttestedCredentialData()) {
        return rejected("no attested credential data");
      }
      if (WebAuthnSupport.coseAlgorithm(authData.credentialPublicKey()) != WebAuthnSupport.ES256) {
        return rejected("only ES256 credentials are accepted");
      }
      WebAuthnSupport.toPublicKey(authData.credentialPublicKey());
      if (!WebAuthnSupport.verifyAttestation(attestation, clientData.hash()).valid()) {
        return rejected("invalid attestation statement");
      }
      String credentialId = WebAuthnSupport.base64Url(authData.credentialId());
      if (state.bankCredential(credentialId).isPresent()) {
        return rejected("credential is already registered");
      }

      // The options endpoint sent Tom's user handle to the browser, the browser sends it back
      // and we use it to find out which account the new key belongs to.
      byte[] userHandle = WebAuthnSupport.fromBase64Url((String) body.get("userHandle"));
      String account = new String(userHandle, StandardCharsets.UTF_8);
      if (!WebAuthnLessonState.ACCOUNTS.contains(account)) {
        return failed(this)
            .feedback("webauthn.registration.unknown-account")
            .feedbackArgs(HtmlUtils.htmlEscape(account))
            .build();
      }

      state.addBankCredential(
          new StoredCredential(
              credentialId,
              account,
              userHandle,
              authData.credentialPublicKey(),
              new AtomicLong(authData.signCount()),
              attestation.fmt(),
              authData.aaguidAsUuid()));

      String output =
          "Credential %s registered for account <b>%s</b><br/>clientDataJSON: %s<br/>flags: %s"
              .formatted(
                  credentialId,
                  account,
                  HtmlUtils.htmlEscape(new String(clientData.raw(), StandardCharsets.UTF_8)),
                  authData.flagsDescription());
      if (JERRY.equals(account)) {
        return success(this).feedback("webauthn.registration.success").output(output).build();
      }
      return informationMessage(this)
          .feedback("webauthn.registration.registered")
          .feedbackArgs(account)
          .output(output)
          .build();
    } catch (IllegalArgumentException e) {
      return rejected(e.getMessage());
    }
  }

  private AttackResult rejected(String reason) {
    return failed(this).feedback("webauthn.registration.rejected").feedbackArgs(HtmlUtils.htmlEscape(String.valueOf(reason)))
        .build();
  }
}
