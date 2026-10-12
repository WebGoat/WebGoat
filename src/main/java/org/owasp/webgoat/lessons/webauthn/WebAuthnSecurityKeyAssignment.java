/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.owasp.webgoat.lessons.webauthn.SecurityKeyCeremonyEndpoints.AUTHENTICATION_CHALLENGE;
import static org.owasp.webgoat.lessons.webauthn.SecurityKeyCeremonyEndpoints.RP_NAME;
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
 * Bonus assignment: sign in with a real roaming authenticator (USB/NFC/BLE security key) that was
 * registered through {@link SecurityKeyCeremonyEndpoints}. Implements the authentication
 * verification steps of the WebAuthn specification.
 */
@RestController
@RequiredArgsConstructor
@AssignmentHints({
  "webauthn.securitykey.hint1",
  "webauthn.securitykey.hint2",
  "webauthn.securitykey.hint3"
})
public class WebAuthnSecurityKeyAssignment implements AssignmentEndpoint {

  private final WebAuthnLessonState lessonState;

  @PostMapping(
      value = "/WebAuthn/securitykey/authentication/finish",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult finishAuthentication(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> credential,
      HttpServletRequest request,
      HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    String expectedChallenge = (String) session.getAttribute(AUTHENTICATION_CHALLENGE);
    session.removeAttribute(AUTHENTICATION_CHALLENGE);
    if (expectedChallenge == null) {
      return rejected("no authentication in progress, request new options first");
    }
    var state = lessonState.forUser(username);

    try {
      StoredCredential stored = state.securityKey(path(credential, "id")).orElse(null);
      if (stored == null) {
        return rejected("credential is not registered for your account");
      }
      String userHandle = path(credential, "response", "userHandle");
      if (userHandle != null
          && !MessageDigest.isEqual(
              stored.userHandle(), WebAuthnSupport.fromBase64Url(userHandle))) {
        return rejected("user handle does not belong to the credential owner");
      }

      ClientData clientData =
          WebAuthnSupport.parseClientData(path(credential, "response", "clientDataJSON"));
      if (!"webauthn.get".equals(clientData.type())) {
        return rejected("clientDataJSON.type must be 'webauthn.get'");
      }
      if (!WebAuthnSupport.sameBase64Url(expectedChallenge, clientData.challenge())) {
        return rejected("challenge mismatch (replayed assertion?)");
      }
      if (!rp.origin().equals(clientData.origin())) {
        return rejected("origin mismatch");
      }

      AuthenticatorData authData =
          WebAuthnSupport.parseAuthenticatorData(
              WebAuthnSupport.fromBase64Url(path(credential, "response", "authenticatorData")));
      if (!MessageDigest.isEqual(rp.idHash(), authData.rpIdHash())) {
        return rejected("rpIdHash mismatch");
      }
      if (!authData.userPresent()) {
        return rejected("user presence flag not set, touch your security key");
      }

      byte[] signedData = WebAuthnSupport.concat(authData.raw(), clientData.hash());
      byte[] signature = WebAuthnSupport.fromBase64Url(path(credential, "response", "signature"));
      if (!WebAuthnSupport.verifySignature(
          stored.algorithm(), stored.publicKey(), signedData, signature)) {
        return rejected("signature verification failed");
      }

      long storedCount = stored.signCount().get();
      long newCount = authData.signCount();
      if ((storedCount != 0 || newCount != 0) && newCount <= storedCount) {
        return rejected(
            "signature counter went from %d to %d, the key may be cloned"
                .formatted(storedCount, newCount));
      }
      stored.signCount().set(newCount);

      return success(this)
          .feedback("webauthn.securitykey.success")
          .output(
              "Verified assertion of credential %s<br/>AAGUID: %s, attestation: %s<br/>flags: %s,"
                      .formatted(stored.credentialId(), stored.aaguid(), stored.attestation(),
                          authData.flagsDescription())
                  + " signCount: %d -> %d".formatted(storedCount, newCount))
          .build();
    } catch (IllegalArgumentException e) {
      return rejected(e.getMessage());
    }
  }

  private AttackResult rejected(String reason) {
    return failed(this)
        .feedback("webauthn.securitykey.rejected")
        .feedbackArgs(HtmlUtils.htmlEscape(String.valueOf(reason)))
        .build();
  }
}
