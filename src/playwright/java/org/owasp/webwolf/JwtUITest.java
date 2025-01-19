package org.owasp.webwolf;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.PlaywrightTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class JwtUITest extends PlaywrightTest {

  @Test
  void shouldDecodeJwt(Page page) {
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
            "{\n  \"iat\" : 1516239022,\n  \"name\" : \"John Doe\",\n  \"sub\" : \"1234567890\"\n}");
  }
}
