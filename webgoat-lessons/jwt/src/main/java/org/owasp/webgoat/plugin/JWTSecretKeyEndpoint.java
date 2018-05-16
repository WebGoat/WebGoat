package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/secret")
@AssignmentHints({"jwt-secret-hint1", "jwt-secret-hint2", "jwt-secret-hint3"})
public class JWTSecretKeyEndpoint extends AssignmentEndpoint {

    private static final String JWT_SECRET = "victory";
    private static final String WEBGOAT_USER = "WebGoat";

    @PostMapping()
    public void login(@RequestParam String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJwt(token);
            Claims claims = (Claims) jwt.getBody();
            String user = (String) claims.get("username");

            if (WEBGOAT_USER.equalsIgnoreCase(user)) {
                trackProgress(success().build());
            } else {
                trackProgress(failed().feedback("jwt-secret.not-correct").feedbackArgs(user).build());
            }
        } catch (Exception e) {
            trackProgress(failed().feedback("jwt-invalid-token").output(e.getMessage()).build());
        }
    }
}
