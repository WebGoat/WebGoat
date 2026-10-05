/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webwolf;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;

import com.microsoft.playwright.options.AriaRole;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

class JwtUITest extends PlaywrightTest {

  @Test
  void shouldDecodeJwt(Browser browser) {
    var page = Authentication.sylvester(browser);
    var secretKey = "test";
    var jwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    page.navigate(webWolfURL("jwt"));
    page.getByPlaceholder("Enter your secret key").fill(secretKey);
    page.getByPlaceholder("Paste token here").type(jwt);
    assertThat(page.locator("#header"))
        .hasValue("{\n  \"alg\" : \"HS256\",\n  \"typ\" : \"JWT\"\n}");
    assertThat(page.locator("#payload"))
        .hasValue(
            "{\n"
                + "  \"iat\" : 1516239022,\n"
                + "  \"name\" : \"John Doe\",\n"
                + "  \"sub\" : \"1234567890\"\n"
                + "}");
  }

  @Test
  void shouldValidateJwtUsingJwks(Browser browser) throws JoseException {
    var page = Authentication.sylvester(browser);

    RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(2048);
    jwk.setKeyId("kid-1");
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload("{\"sub\":\"123\"}");
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    jws.setKey(jwk.getPrivateKey());
    jws.setKeyIdHeaderValue(jwk.getKeyId());
    var rsaJwt = jws.getCompactSerialization();
    var jwks = new JsonWebKeySet(jwk).toJson(OutputControlLevel.PUBLIC_ONLY);

    page.navigate(webWolfURL("jwt"));
    page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("JWKS (public keys)")).check();
    page.getByPlaceholder("Paste token here").type(rsaJwt);
    page.locator("#jwks").fill(jwks);
    assertThat(page.locator("#signatureValid")).hasText("Signature valid");
  }
}
