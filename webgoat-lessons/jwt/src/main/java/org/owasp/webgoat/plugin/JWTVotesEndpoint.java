package org.owasp.webgoat.plugin;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.votes.Views;
import org.owasp.webgoat.plugin.votes.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static java.util.Comparator.comparingLong;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@AssignmentPath("/JWT/votings")
@AssignmentHints({"jwt-change-token-hint1", "jwt-change-token-hint2", "jwt-change-token-hint3", "jwt-change-token-hint4", "jwt-change-token-hint5"})
public class JWTVotesEndpoint extends AssignmentEndpoint {

    public static final String JWT_PASSWORD = TextCodec.BASE64.encode("victory");
    private static String validUsers = "TomJerrySylvester";

    private static int totalVotes = 38929;
    private Map<String, Vote> votes = Maps.newHashMap();

    @PostConstruct
    public void initVotes() {
        votes.put("Admin lost password", new Vote("Admin lost password",
                "In this challenge you will need to help the admin and find the password in order to login",
                "challenge1-small.png", "challenge1.png", 36000, totalVotes));
        votes.put("Vote for your favourite",
                new Vote("Vote for your favourite",
                        "In this challenge ...",
                        "challenge5-small.png", "challenge5.png", 30000, totalVotes));
        votes.put("Get it for free",
                new Vote("Get it for free",
                        "The objective for this challenge is to buy a Samsung phone for free.",
                        "challenge2-small.png", "challenge2.png", 20000, totalVotes));
        votes.put("Photo comments",
                new Vote("Photo comments",
                        "n this challenge you can comment on the photo you will need to find the flag somewhere.",
                        "challenge3-small.png", "challenge3.png", 10000, totalVotes));
    }

    @GetMapping("/login")
    public void login(@RequestParam("user") String user, HttpServletResponse response) {
        if (validUsers.contains(user)) {
            Claims claims = Jwts.claims().setIssuedAt(Date.from(Instant.now().plus(Duration.ofDays(10))));
            claims.put("admin", "false");
            claims.put("user", user);
            String token = Jwts.builder()
                    .setClaims(claims)
                    .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, JWT_PASSWORD)
                    .compact();
            Cookie cookie = new Cookie("access_token", token);
            response.addCookie(cookie);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } else {
            Cookie cookie = new Cookie("access_token", "");
            response.addCookie(cookie);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }

    @GetMapping
    @ResponseBody
    public MappingJacksonValue getVotes(@CookieValue(value = "access_token", required = false) String accessToken) {
        MappingJacksonValue value = new MappingJacksonValue(votes.values().stream().sorted(comparingLong(Vote::getAverage).reversed()).collect(toList()));
        if (StringUtils.isEmpty(accessToken)) {
            value.setSerializationView(Views.GuestView.class);
        } else {
            try {
                Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(accessToken);
                Claims claims = (Claims) jwt.getBody();
                String user = (String) claims.get("user");
                if ("Guest".equals(user) || !validUsers.contains(user)) {
                    value.setSerializationView(Views.GuestView.class);
                } else {
                    value.setSerializationView(Views.UserView.class);
                }
            } catch (JwtException e) {
                value.setSerializationView(Views.GuestView.class);
            }
        }
        return value;
    }

    @PostMapping(value = "{title}")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> vote(@PathVariable String title, @CookieValue(value = "access_token", required = false) String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            try {
                Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(accessToken);
                Claims claims = (Claims) jwt.getBody();
                String user = (String) claims.get("user");
                if (!validUsers.contains(user)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                } else {
                    ofNullable(votes.get(title)).ifPresent(v -> v.incrementNumberOfVotes(totalVotes));
                    return ResponseEntity.accepted().build();
                }
            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }

    @PostMapping("reset")
    public @ResponseBody
    AttackResult resetVotes(@CookieValue(value = "access_token", required = false) String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return trackProgress(failed().feedback("jwt-invalid-token").build());
        } else {
            try {
                Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(accessToken);
                Claims claims = (Claims) jwt.getBody();
                boolean isAdmin = Boolean.valueOf((String) claims.get("admin"));
                if (!isAdmin) {
                    return trackProgress(failed().feedback("jwt-only-admin").build());
                } else {
                    votes.values().forEach(vote -> vote.reset());
                    return trackProgress(success().build());
                }
            } catch (JwtException e) {
                return trackProgress(failed().feedback("jwt-invalid-token").output(e.toString()).build());
            }
        }
    }
}
