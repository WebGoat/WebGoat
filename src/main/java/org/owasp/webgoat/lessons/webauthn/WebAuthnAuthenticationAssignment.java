/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.informationMessage;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.owasp.webgoat.lessons.webauthn.GoatBankCeremonyEndpoints.AUTHENTICATION_CHALLENGE;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.JERRY;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.RP_NAME;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.path;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.StoredCredential;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.AuthenticatorData;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.ClientData;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.RelyingParty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * GoatBank's login endpoint. The signature is verified, but the assertion is never tied to the
 * challenge issued for this login attempt, so a recorded assertion can be replayed.
 */
@RestController
@RequiredArgsConstructor
@AssignmentHints({
  "webauthn.authentication.hint1",
  "webauthn.authentication.hint2",
  "webauthn.authentication.hint3",
  "webauthn.authentication.hint4"
})
public class WebAuthnAuthenticationAssignment implements AssignmentEndpoint {

  private final WebAuthnLessonState lessonState;

  @PostMapping(
      value = "/WebAuthn/authentication/finish",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult finishAuthentication(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> body,
      HttpServletRequest request,
      HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    // the challenge is single use, so it is removed from the session...
    session.removeAttribute(AUTHENTICATION_CHALLENGE);

    try {
      String account = String.valueOf(body.get("username"));
      StoredCredential credential =
          lessonState.forUser(username).bankCredential(path(body, "credential", "id")).orElse(null);
      if (credential == null || !credential.account().equals(account)) {
        return rejected("unknown credential for this account");
      }

      ClientData clientData =
          WebAuthnSupport.parseClientData(path(body, "credential", "response", "clientDataJSON"));
      if (!"webauthn.get".equals(clientData.type())) {
        return rejected("clientDataJSON.type must be 'webauthn.get'");
      }
      // ...but never compared with clientData.challenge()
      if (!rp.origin().equals(clientData.origin())) {
        return rejected("unexpected origin " + clientData.origin());
      }

      AuthenticatorData authData =
          WebAuthnSupport.parseAuthenticatorData(
              WebAuthnSupport.fromBase64Url(
                  path(body, "credential", "response", "authenticatorData")));
      if (!MessageDigest.isEqual(rp.idHash(), authData.rpIdHash())) {
        return rejected("rpIdHash does not match " + rp.id());
      }
      if (!authData.userPresent()) {
        return rejected("user presence flag not set");
      }

      byte[] signedData = WebAuthnSupport.concat(authData.raw(), clientData.hash());
      byte[] signature =
          WebAuthnSupport.fromBase64Url(path(body, "credential", "response", "signature"));
      if (!WebAuthnSupport.verifySignature(
          credential.algorithm(), credential.publicKey(), signedData, signature)) {
        return rejected("signature verification failed");
      }

      // many passkeys always report 0, so the counter is stored but not enforced
      credential.signCount().set(authData.signCount());

      String output =
          "Signed in as <b>%s</b> (signCount %d, challenge in assertion: %s)"
              .formatted(
                  account,
                  authData.signCount(),
                  HtmlUtils.htmlEscape(String.valueOf(clientData.challenge())));
      if (JERRY.equals(account)) {
        return success(this).feedback("webauthn.authentication.success").output(output).build();
      }
      return informationMessage(this)
          .feedback("webauthn.authentication.signed-in")
          .feedbackArgs(account)
          .output(output)
          .build();
    } catch (IllegalArgumentException e) {
      return rejected(e.getMessage());
    }
  }

  private AttackResult rejected(String reason) {
    return failed(this)
        .feedback("webauthn.authentication.rejected")
        .feedbackArgs(HtmlUtils.htmlEscape(String.valueOf(reason)))
        .build();
  }
}
