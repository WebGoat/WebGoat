package org.owasp.webgoat.plugin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.hamcrest.CoreMatchers;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.plugin.JWTSecretKeyEndpoint.JWT_SECRET;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class JWTSecretKeyEndpointTest extends LessonTest {

    @Before
    public void setup() {
        JWT jwt = new JWT();
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    private Claims createClaims(String username) {
        Claims claims = Jwts.claims();
        claims.put("admin", "true");
        claims.put("user", "Tom");
        claims.setExpiration(Date.from(Instant.now().plus(Duration.ofDays(1))));
        claims.setIssuedAt(Date.from(Instant.now().plus(Duration.ofDays(1))));
        claims.setIssuer("iss");
        claims.setAudience("aud");
        claims.setSubject("sub");
        claims.put("username", username);
        claims.put("Email", "webgoat@webgoat.io");
        claims.put("Role", new String[]{"user"});
        return claims;
    }

    @Test
    public void solveAssignment() throws Exception {
        Claims claims = createClaims("WebGoat");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void solveAssignmentWithLowercase() throws Exception {
        Claims claims = createClaims("webgoat");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void oneOfClaimIsMissingShouldNotSolveAssignment() throws Exception {
        Claims claims = createClaims("WebGoat");
        claims.remove("aud");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-secret-claims-missing"))));
    }

    @Test
    public void incorrectUser() throws Exception {
        Claims claims = createClaims("Tom");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, JWT_SECRET).compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-secret-incorrect-user", "default", "Tom"))));
    }

    @Test
    public void incorrectToken() throws Exception {
        Claims claims = createClaims("Tom");
        String token = Jwts.builder().setClaims(claims).signWith(HS512, "wrong_password").compact();

        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/secret")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
    }
}