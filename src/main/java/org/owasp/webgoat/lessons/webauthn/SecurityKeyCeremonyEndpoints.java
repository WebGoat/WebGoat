/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.base64Url;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.path;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.StoredCredential;
import org.owasp.webgoat.lessons.webauthn.WebAuthnLessonState.UserState;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.AttestationObject;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.AttestationResult;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.AuthenticatorData;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.ClientData;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.RelyingParty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration of a real roaming authenticator (security key) using the browser WebAuthn API. In
 * contrast to GoatBank this implements the verification steps of the WebAuthn specification.
 */
@RestController
@RequiredArgsConstructor
public class SecurityKeyCeremonyEndpoints {

  static final String RP_NAME = "WebGoat";
  static final String REGISTRATION_CHALLENGE = "webauthn.securitykey.registration.challenge";
  static final String AUTHENTICATION_CHALLENGE = "webauthn.securitykey.authentication.challenge";
  static final List<Long> ALLOWED_ALGORITHMS =
      List.of(WebAuthnSupport.ES256, WebAuthnSupport.EDDSA, WebAuthnSupport.RS256);

  private final WebAuthnLessonState lessonState;

  @PostMapping(
      value = "/WebAuthn/securitykey/registration/options",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> registrationOptions(
      @CurrentUsername String username, HttpServletRequest request, HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    UserState state = lessonState.forUser(username);
    String challenge = WebAuthnSupport.randomChallenge();
    session.setAttribute(REGISTRATION_CHALLENGE, challenge);

    Map<String, Object> options = new LinkedHashMap<>();
    options.put("challenge", challenge);
    options.put("rp", Map.of("id", rp.id(), "name", rp.name()));
    // the user handle is a random value, not the user name
    options.put(
        "user",
        Map.of(
            "id", base64Url(state.securityKeyUserHandle()),
            "name", username,
            "displayName", username));
    options.put(
        "pubKeyCredParams",
        ALLOWED_ALGORITHMS.stream().map(alg -> Map.of("type", "public-key", "alg", alg)).toList());
    options.put("timeout", 120000);
    options.put("attestation", "direct");
    options.put(
        "authenticatorSelection",
        Map.of(
            "authenticatorAttachment", "cross-platform",
            "residentKey", "discouraged",
            "requireResidentKey", false,
            "userVerification", "discouraged"));
    options.put("hints", List.of("security-key"));
    options.put("excludeCredentials", descriptors(state.securityKeys()));
    return options;
  }

  @PostMapping(
      value = "/WebAuthn/securitykey/registration/finish",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> finishRegistration(
      @CurrentUsername String username,
      @RequestBody Map<String, Object> credential,
      HttpServletRequest request,
      HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    UserState state = lessonState.forUser(username);
    // challenges are single use
    String expectedChallenge = (String) session.getAttribute(REGISTRATION_CHALLENGE);
    session.removeAttribute(REGISTRATION_CHALLENGE);
    if (expectedChallenge == null) {
      return error("No registration in progress, request new options first");
    }
    try {
      if (!"public-key".equals(credential.get("type"))) {
        return error("credential.type must be 'public-key'");
      }
      ClientData clientData =
          WebAuthnSupport.parseClientData(path(credential, "response", "clientDataJSON"));
      if (!"webauthn.create".equals(clientData.type())) {
        return error("clientDataJSON.type must be 'webauthn.create'");
      }
      if (!WebAuthnSupport.sameBase64Url(expectedChallenge, clientData.challenge())) {
        return error("challenge mismatch");
      }
      if (!rp.origin().equals(clientData.origin())) {
        return error("origin mismatch, expected " + rp.origin());
      }

      AttestationObject attestation =
          WebAuthnSupport.parseAttestationObject(path(credential, "response", "attestationObject"));
      AuthenticatorData authData = attestation.authData();
      if (!MessageDigest.isEqual(rp.idHash(), authData.rpIdHash())) {
        return error("rpIdHash mismatch, expected hash of " + rp.id());
      }
      if (!authData.userPresent()) {
        return error("user presence flag not set");
      }
      if (!authData.hasAttestedCredentialData()) {
        return error("no attested credential data");
      }
      long alg = WebAuthnSupport.coseAlgorithm(authData.credentialPublicKey());
      if (!ALLOWED_ALGORITHMS.contains(alg)) {
        return error("algorithm %d was not requested".formatted(alg));
      }
      WebAuthnSupport.toPublicKey(authData.credentialPublicKey());
      AttestationResult attestationResult =
          WebAuthnSupport.verifyAttestation(attestation, clientData.hash());
      if (!attestationResult.valid()) {
        return error(attestationResult.description());
      }
      String credentialId = base64Url(authData.credentialId());
      if (!credentialId.equals(credential.get("id"))) {
        return error("credential id does not match the attested credential");
      }
      if (state.securityKey(credentialId).isPresent()) {
        return error("this security key is already registered");
      }

      state.addSecurityKey(
          new StoredCredential(
              credentialId,
              username,
              state.securityKeyUserHandle(),
              authData.credentialPublicKey(),
              new AtomicLong(authData.signCount()),
              attestation.fmt(),
              authData.aaguidAsUuid()));

      Map<String, Object> result = new LinkedHashMap<>();
      result.put("registered", true);
      result.put("credentialId", credentialId);
      result.put("algorithm", alg);
      result.put("aaguid", authData.aaguidAsUuid());
      result.put("attestationFormat", attestation.fmt());
      result.put("attestationType", attestationResult.type());
      result.put("attestation", attestationResult.description());
      result.put("flags", authData.flagsDescription());
      result.put("signCount", authData.signCount());
      // client reported values, informative only: the server cannot verify them
      result.put("authenticatorAttachment", credential.get("authenticatorAttachment"));
      result.put(
          "transports",
          credential.get("response") instanceof Map<?, ?> response
              ? response.get("transports")
              : null);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
      return error(e.getMessage());
    }
  }

  @PostMapping(
      value = "/WebAuthn/securitykey/authentication/options",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> authenticationOptions(
      @CurrentUsername String username, HttpServletRequest request, HttpSession session) {
    RelyingParty rp = RelyingParty.from(request, RP_NAME);
    List<StoredCredential> keys = lessonState.forUser(username).securityKeys();
    if (keys.isEmpty()) {
      return error("Register a security key first");
    }
    String challenge = WebAuthnSupport.randomChallenge();
    session.setAttribute(AUTHENTICATION_CHALLENGE, challenge);

    Map<String, Object> options = new LinkedHashMap<>();
    options.put("challenge", challenge);
    options.put("rpId", rp.id());
    options.put("allowCredentials", descriptors(keys));
    options.put("userVerification", "discouraged");
    options.put("hints", List.of("security-key"));
    options.put("timeout", 120000);
    return ResponseEntity.ok(options);
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
