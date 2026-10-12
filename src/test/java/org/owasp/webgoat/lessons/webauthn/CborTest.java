/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CborTest {

  @Test
  void roundTripsWebAuthnStructures() {
    Map<Object, Object> map = new LinkedHashMap<>();
    map.put("fmt", "none");
    map.put("attStmt", Map.of());
    map.put(1L, 2L);
    map.put(-7L, new byte[] {1, 2, 3});
    map.put("list", List.of(true, false, 70000L, -300L));

    Object decoded = Cbor.decode(Cbor.encode(map));

    assertThat(decoded).isInstanceOf(Map.class);
    Map<?, ?> result = (Map<?, ?>) decoded;
    assertThat(result.get("fmt")).isEqualTo("none");
    assertThat(result.get(1L)).isEqualTo(2L);
    assertThat((byte[]) result.get(-7L)).containsExactly(1, 2, 3);
    assertThat(result.get("list")).isEqualTo(List.of(true, false, 70000L, -300L));
  }

  @Test
  void decodesRfc8949Examples() {
    assertThat(Cbor.decode(HexFormat.of().parseHex("1903e8"))).isEqualTo(1000L);
    assertThat(Cbor.decode(HexFormat.of().parseHex("3903e7"))).isEqualTo(-1000L);
    assertThat(Cbor.decode(HexFormat.of().parseHex("6449455446"))).isEqualTo("IETF");
  }

  @Test
  void rejectsMalformedInput() {
    // byte string claiming 16 bytes, only 1 present
    assertThatThrownBy(() -> Cbor.decode(HexFormat.of().parseHex("5001")))
        .isInstanceOf(IllegalArgumentException.class);
    // indefinite length array
    assertThatThrownBy(() -> Cbor.decode(HexFormat.of().parseHex("9f01ff")))
        .isInstanceOf(IllegalArgumentException.class);
    // trailing data
    assertThatThrownBy(() -> Cbor.decode(HexFormat.of().parseHex("0101")))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void virtualAuthenticatorProducesParsableAttestation() {
    var authenticator = new VirtualAuthenticator();
    var credential =
        authenticator.create("http://localhost", "localhost", "Y2hhbGxlbmdl", new byte[] {1});
    var response = (Map<?, ?>) credential.get("response");

    var attestation =
        WebAuthnSupport.parseAttestationObject((String) response.get("attestationObject"));
    var clientData = WebAuthnSupport.parseClientData((String) response.get("clientDataJSON"));

    assertThat(attestation.fmt()).isEqualTo("none");
    assertThat(attestation.authData().userPresent()).isTrue();
    assertThat(attestation.authData().aaguidAsUuid())
        .isEqualTo(VirtualAuthenticator.AAGUID.toString());
    assertThat(WebAuthnSupport.base64Url(attestation.authData().credentialId()))
        .isEqualTo(credential.get("id"));
    assertThat(WebAuthnSupport.toPublicKey(attestation.authData().credentialPublicKey()))
        .isNotNull();
    assertThat(clientData.type()).isEqualTo("webauthn.create");
    assertThat(WebAuthnSupport.verifyAttestation(attestation, clientData.hash()).valid()).isTrue();
  }
}
