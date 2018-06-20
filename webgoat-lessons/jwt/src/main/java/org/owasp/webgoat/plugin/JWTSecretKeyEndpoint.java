package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import io.jsonwebtoken.impl.TextCodec;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/secret")
@AssignmentHints({"jwt-secret-hint1", "jwt-secret-hint2", "jwt-secret-hint3"})
public class JWTSecretKeyEndpoint extends AssignmentEndpoint {

    public static final String JWT_SECRET = TextCodec.BASE64.encode("victory");
    private static final String WEBGOAT_USER = "WebGoat";
    private static final List<String> expectedClaims = Lists.newArrayList("iss", "iat", "exp", "aud", "sub", "username", "Email", "Role");

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
            return trackProgress(failed().feedback("jwt-invalid-token").output(e.getMessage()).build());
        }
    }
}
