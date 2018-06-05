package org.owasp.webgoat.plugin;

import com.google.common.base.Charsets;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <pre>
 *  {
 *      "typ": "JWT",
 *      "kid": "webgoat_key",
 *      "alg": "HS256"
 *  }
 *  {
 *       "iss": "WebGoat Token Builder",
 *       "iat": 1524210904,
 *       "exp": 1618905304,
 *       "aud": "webgoat.org",
 *       "sub": "jerry@webgoat.com",
 *       "username": "Jerry",
 *       "Email": "jerry@webgoat.com",
 *       "Role": [
 *       "Cat"
 *       ]
 *  }
 * </pre>
 *
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/final")
@AssignmentHints({"jwt-final-hint1", "jwt-final-hint2", "jwt-final-hint3", "jwt-final-hint4", "jwt-final-hint5", "jwt-final-hint6"})
public class JWTFinalEndpoint extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;

    @PostMapping("follow/{user}")
    public @ResponseBody
    String follow(@PathVariable("user") String user) {
        if ("Jerry".equals(user)) {
            return "Following yourself seems redundant";
        } else {
            return "You are now following Tom";
        }
    }

    @PostMapping("delete")
    public @ResponseBody
    AttackResult resetVotes(@RequestParam("token") String token) {
        if (StringUtils.isEmpty(token)) {
            return trackProgress(failed().feedback("jwt-invalid-token").build());
        } else {
            try {
                final String[] errorMessage = {null};
                Jwt jwt = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                        final String kid = (String) header.get("kid");
                        try {
                            Connection connection = DatabaseUtilities.getConnection(webSession);
                            ResultSet rs = connection.createStatement().executeQuery("SELECT key FROM jwt_keys WHERE id = '" + kid + "'");
                            while (rs.next()) {
                                return TextCodec.BASE64.decode(rs.getString(1));
                            }
                        } catch (SQLException e) {
                            errorMessage[0] = e.getMessage();
                        }
                        return null;
                    }
                }).parse(token);
                if (errorMessage[0] != null) {
                    return trackProgress(failed().output(errorMessage[0]).build());
                }
                Claims claims = (Claims) jwt.getBody();
                String username = (String) claims.get("username");
                if ("Jerry".equals(username)) {
                    return trackProgress(failed().feedback("jwt-final-jerry-account").build());
                }
                if ("Tom".equals(username)) {
                    return trackProgress(success().build());
                } else {
                    return trackProgress(failed().feedback("jwt-final-not-tom").build());
                }
            } catch (JwtException e) {
                return trackProgress(failed().feedback("jwt-invalid-token").output(e.toString()).build());
            }
        }
    }
}
