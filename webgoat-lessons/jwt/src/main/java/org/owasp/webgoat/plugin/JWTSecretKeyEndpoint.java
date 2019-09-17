package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import io.jsonwebtoken.impl.TextCodec;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/secret")
@AssignmentHints({"jwt-secret-hint1", "jwt-secret-hint2", "jwt-secret-hint3"})
public class JWTSecretKeyEndpoint extends AssignmentEndpoint {

	public static final String[] SECRETS = {"victory","business","available", "shipping", "washington"};
    public static final String JWT_SECRET = TextCodec.BASE64.encode(SECRETS[new Random().nextInt(SECRETS.length)]);
    private static final String WEBGOAT_USER = "WebGoat";
    private static final List<String> expectedClaims = Lists.newArrayList("iss", "iat", "exp", "aud", "sub", "username", "Email", "Role");
    
    @RequestMapping(path="/gettoken",produces=MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getSecretToken() {
    	return Jwts.builder()
    		.setIssuer("WebGoat Token Builder")
    		.setAudience("webgoat.org")
    		.setIssuedAt(Calendar.getInstance().getTime())
    		.setExpiration(Date.from(Instant.now().plusSeconds(60)))
    		.setSubject("tom@webgoat.org")
    		.claim("username", "Tom")
    		.claim("Email", "tom@webgoat.org")
    		.claim("Role", new String[] {"Manager", "Project Administrator"})
    		.signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }
    
    @PostMapping
    @ResponseBody
    public AttackResult login(@RequestParam String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(JWT_SECRET).parse(token);
            Claims claims = (Claims) jwt.getBody();
            if (!claims.keySet().containsAll(expectedClaims)) {
                return trackProgress(failed().feedback("jwt-secret-claims-missing").build());
            } else {
                String user = (String) claims.get("username");

                if (WEBGOAT_USER.equalsIgnoreCase(user)) {
                    return trackProgress(success().build());
                } else {
                    return trackProgress(failed().feedback("jwt-secret-incorrect-user").feedbackArgs(user).build());
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return trackProgress(failed().feedback("jwt-invalid-token").output(e.getMessage()).build());
        }
    }
}
