/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.RelyingParty;
import org.springframework.stereotype.Component;

/**
 * Credential storage of the fictional "GoatBank" relying party, kept per WebGoat user so students
 * cannot interfere with each other. Restarting the lesson clears the state.
 */
@Component
public class WebAuthnLessonState implements Initializable {

  public static final String TOM = "tom";
  public static final String JERRY = "jerry";
  public static final Set<String> ACCOUNTS = Set.of(TOM, JERRY);
  public static final String RP_NAME = "GoatBank";

  public record StoredCredential(
      String credentialId,
      String account,
      byte[] userHandle,
      Map<Object, Object> coseKey,
      AtomicLong signCount,
      String attestation,
      String aaguid) {

    public PublicKey publicKey() {
      return WebAuthnSupport.toPublicKey(coseKey);
    }

    public long algorithm() {
      return WebAuthnSupport.coseAlgorithm(coseKey);
    }
  }

  public static final class UserState {
    private final VirtualAuthenticator tomsSecurityKey = new VirtualAuthenticator();
    private final Map<String, StoredCredential> bankCredentials = new ConcurrentHashMap<>();
    private final Map<String, StoredCredential> securityKeys = new ConcurrentHashMap<>();
    private final byte[] securityKeyUserHandle = WebAuthnSupport.randomBytes(32);

    public VirtualAuthenticator tomsSecurityKey() {
      return tomsSecurityKey;
    }

    public Optional<StoredCredential> bankCredential(String credentialId) {
      return Optional.ofNullable(credentialId).map(bankCredentials::get);
    }

    public List<StoredCredential> bankCredentialsOf(String account) {
      return bankCredentials.values().stream().filter(c -> c.account().equals(account)).toList();
    }

    public void addBankCredential(StoredCredential credential) {
      bankCredentials.put(credential.credentialId(), credential);
    }

    public Optional<StoredCredential> securityKey(String credentialId) {
      return Optional.ofNullable(credentialId).map(securityKeys::get);
    }

    public List<StoredCredential> securityKeys() {
      return List.copyOf(securityKeys.values());
    }

    public void addSecurityKey(StoredCredential credential) {
      securityKeys.put(credential.credentialId(), credential);
    }

    public byte[] securityKeyUserHandle() {
      return securityKeyUserHandle.clone();
    }
  }

  private final Map<String, UserState> users = new ConcurrentHashMap<>();

  // Jerry's security key is shared by all users; only its public key is stored per user.
  private final VirtualAuthenticator jerrysSecurityKey = new VirtualAuthenticator();
  private final String jerrysCredentialId = jerrysSecurityKey.createKey(userHandle(JERRY), 40);
  private final Map<String, Map<String, Object>> capturedLogins = new ConcurrentHashMap<>();

  /**
   * GoatBank derives the WebAuthn user handle from the user name. The specification requires an
   * opaque random value without personal information, this makes handles guessable.
   */
  public static byte[] userHandle(String account) {
    return account.getBytes(StandardCharsets.UTF_8);
  }

  public UserState forUser(String webGoatUser) {
    return users.computeIfAbsent(webGoatUser, u -> newState());
  }

  @Override
  public void initialize(WebGoatUser user) {
    users.remove(user.getUsername());
  }

  private UserState newState() {
    UserState state = new UserState();
    String tomsCredentialId = state.tomsSecurityKey().createKey(userHandle(TOM), 0);
    state.addBankCredential(
        new StoredCredential(
            tomsCredentialId,
            TOM,
            userHandle(TOM),
            state.tomsSecurityKey().coseKey(tomsCredentialId),
            new AtomicLong(),
            "none",
            VirtualAuthenticator.AAGUID.toString()));
    state.addBankCredential(
        new StoredCredential(
            jerrysCredentialId,
            JERRY,
            userHandle(JERRY),
            jerrysSecurityKey.coseKey(jerrysCredentialId),
            new AtomicLong(40),
            "none",
            VirtualAuthenticator.AAGUID.toString()));
    return state;
  }

  /**
   * The request body of one of Jerry's earlier logins, as it ended up in a support ticket. It is
   * generated once per origin so the origin and RP ID hash inside match the current deployment.
   */
  public Map<String, Object> capturedLoginOfJerry(RelyingParty rp) {
    return capturedLogins.computeIfAbsent(
        rp.origin(),
        origin -> {
          Map<String, Object> body = new LinkedHashMap<>();
          body.put("username", JERRY);
          body.put(
              "credential",
              jerrysSecurityKey.get(
                  origin,
                  rp.id(),
                  WebAuthnSupport.randomChallenge(),
                  List.of(jerrysCredentialId)));
          return body;
        });
  }
}
