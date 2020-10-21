package org.owasp.webgoat.jwt;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

@RunWith(SpringJUnit4ClassRunner.class)
public class JWTFinalEndpointTest extends LessonTest {

    private static final String TOKEN_JERRY = "eyJraWQiOiJ3ZWJnb2F0X2tleSIsImFsZyI6IkhTNTEyIn0.eyJhdWQiOiJ3ZWJnb2F0Lm9yZyIsImVtYWlsIjoiamVycnlAd2ViZ29hdC5jb20iLCJ1c2VybmFtZSI6IkplcnJ5In0.xBc5FFwaOcuxjdr_VJ16n8Jb7vScuaZulNTl66F2MWF1aBe47QsUosvbjWGORNcMPiPNwnMu1Yb0WZVNrp2ZXA";

    @Autowired
    private JWT jwt;
    
    @Autowired
    private JWTFinalEndpoint jwtFinalEndpoint;

    @Before
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(jwt);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void solveAssignment() throws Exception {
        String key = "deletingTom";
        Map<String, Object> claims = new HashMap<>();
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

    @Test
    public void shouldNotBeAbleToBypassWithSimpleToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/JWT/final/delete")
                .param("token", ".eyJ1c2VybmFtZSI6IlRvbSJ9.")
                .content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
    }
    
    @Test
    @SneakyThrows
    public void testJWTTestTools() {
    	   
		//JWTFinalEndpoint jwtFinalEndpoint = new JWTFinalEndpoint(null);
		String jsonHeader = "{\"alg\":\"HS256\"}";
		String jsonPayload = "{\"iss\":\"OWASP\"}";
		String jsonSecret = "secret";
		String jwtToken = jwtFinalEndpoint.encode(jsonHeader, jsonPayload, jsonSecret).replace(":", "")
				.replace("encodedHeader", "").replace("encodedPayload", "").replace("encodedSignature", "")
				.replace("{", "").replace("}", "").replace("\"", "").replace(",", ".");

		Jwt jwt = Jwts.parser().setSigningKey(jsonSecret).parse(jwtToken);   
		String revert = jwtFinalEndpoint.decode(jwtToken);
		//System.out.println("revert: "+revert);
        
    }
}