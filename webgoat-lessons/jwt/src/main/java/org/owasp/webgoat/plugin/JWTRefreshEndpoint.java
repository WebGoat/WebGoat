package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/refresh/")
@AssignmentHints({"jwt-refresh-hint1", "jwt-refresh-hint2", "jwt-refresh-hint3", "jwt-refresh-hint4"})
public class JWTRefreshEndpoint extends AssignmentEndpoint {

    public static final String PASSWORD = "bm5nhSkxCXZkKRy4";
    private static final String JWT_PASSWORD = "bm5n3SkxCX4kKRy4";
    private static final List<String> validRefreshTokens = Lists.newArrayList();

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity follow(@RequestBody Map<String, Object> json) {
        String user = (String) json.get("user");
        String password = (String) json.get("password");

        if ("Jerry".equals(user) && PASSWORD.equals(password)) {
            return ResponseEntity.ok(createNewTokens(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private Map<String, Object> createNewTokens(String user) {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("admin", "false");
        claims.put("user", user);
        String token = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, JWT_PASSWORD)
                .compact();
        Map<String, Object> tokenJson = Maps.newHashMap();
        String refreshToken = RandomStringUtils.randomAlphabetic(20);
        validRefreshTokens.add(refreshToken);
        tokenJson.put("access_token", token);
        tokenJson.put("refresh_token", refreshToken);
        return tokenJson;
    }

    @PostMapping("checkout")
    public @ResponseBody
    AttackResult checkout(@RequestHeader("Authorization") String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(token.replace("Bearer ", ""));
            Claims claims = (Claims) jwt.getBody();
            String user = (String) claims.get("user");
            if ("Tom".equals(user)) {
                return trackProgress(success().build());
            }
            return trackProgress(failed().feedback("jwt-refresh-not-tom").feedbackArgs(user).build());
        } catch (ExpiredJwtException e) {
            return trackProgress(failed().output(e.getMessage()).build());
        } catch (JwtException e) {
            return trackProgress(failed().feedback("jwt-invalid-token").build());
        }
    }

    @PostMapping("newToken")
    public @ResponseBody
    ResponseEntity newToken(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> json) {
        String user;
        String refreshToken;
        try {
            Jwt<Header, Claims> jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(token.replace("Bearer ", ""));
            user = (String) jwt.getBody().get("user");
            refreshToken = (String) json.get("refresh_token");
        } catch (ExpiredJwtException e) {
            user = (String) e.getClaims().get("user");
            refreshToken = (String) json.get("refresh_token");
        }

        if (user == null || refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (validRefreshTokens.contains(refreshToken)) {
            validRefreshTokens.remove(refreshToken);
            return ResponseEntity.ok(createNewTokens(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
