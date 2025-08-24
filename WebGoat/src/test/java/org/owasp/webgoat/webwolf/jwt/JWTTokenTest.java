/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class JWTTokenTest {

  @Test
  void encodeCorrectTokenWithoutSignature() {
    var headers = Map.of("alg", "HS256", "typ", "JWT");
    var payload = Map.of("test", "test");
    var token = JWTToken.encode(toString(headers), toString(payload), "");

    assertThat(token.getEncoded())
        .isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZXN0IjoidGVzdCJ9");
  }

  @Test
  void encodeCorrectTokenWithSignature() {
    var headers = Map.of("alg", "HS256", "typ", "JWT");
    var payload = Map.of("test", "test");
    var token = JWTToken.encode(toString(headers), toString(payload), "webgoat");

    assertThat(token.getEncoded())
        .isEqualTo(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZXN0IjoidGVzdCJ9.axNp9BkswwK_YRF2URJ5P1UejQNYZbK4qYcMnkusg6I");
  }

  @Test
  void encodeTokenWithNonJsonInput() {
    var token = JWTToken.encode("aaa", "bbb", "test");

    assertThat(token.getEncoded()).isNullOrEmpty();
  }

  @Test
  void decodeValidSignedToken() {
    var token =
        JWTToken.decode(
            "eyJhbGciOiJIUzI1NiJ9.eyJ0ZXN0IjoidGVzdCJ9.KOobRHDYyaesV_doOk11XXGKSONwzllraAaqqM4VFE4",
            "test");

    assertThat(token.getHeader()).contains("\"alg\" : \"HS256\"");
    assertThat(token.isSignatureValid()).isTrue();
  }

  @Test
  void decodeInvalidSignedToken() {
    var token =
        JWTToken.decode(
            "eyJhbGciOiJIUzI1NiJ9.eyJ0ZXsdfdfsaasfddfasN0IjoidGVzdCJ9.KOobRHDYyaesV_doOk11XXGKSONwzllraAaqqM4VFE4",
            "");

    assertThat(token.getHeader()).contains("\"alg\" : \"HS256\"");
    assertThat(token.getPayload()).contains("{\"te");
  }

  @Test
  void onlyEncodeWhenHeaderOrPayloadIsPresent() {
    var token = JWTToken.encode("", "", "");

    assertThat(token.getEncoded()).isNullOrEmpty();
  }

  @Test
  void encodeAlgNone() {
    var headers = Map.of("alg", "none");
    var payload = Map.of("test", "test");
    var token = JWTToken.encode(toString(headers), toString(payload), "test");

    assertThat(token.getEncoded()).isEqualTo("eyJhbGciOiJub25lIn0.eyJ0ZXN0IjoidGVzdCJ9");
  }

  @SneakyThrows
  private String toString(Map<String, String> map) {
    var mapper = new ObjectMapper();
    return mapper.writeValueAsString(map);
  }
}
