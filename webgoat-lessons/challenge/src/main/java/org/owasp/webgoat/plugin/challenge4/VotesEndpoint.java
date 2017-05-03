package org.owasp.webgoat.plugin.challenge4;

import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Comparator.comparingLong;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.owasp.webgoat.plugin.Flag.FLAGS;
import static org.owasp.webgoat.plugin.SolutionConstants.JWT_PASSWORD;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@RestController
@RequestMapping("/votings")
public class VotesEndpoint {

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
            Map<String, Object> claims = Maps.newHashMap();
            claims.put("admin", "false");
            claims.put("user", user);
            String token = Jwts.builder()
                    .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, JWT_PASSWORD)
                    .compact();
            Cookie cookie = new Cookie("access_token", token);
            response.addCookie(cookie);
            response.setStatus(HttpStatus.OK.value());
        } else {
            Cookie cookie = new Cookie("access_token", "");
            response.addCookie(cookie);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @GetMapping
    public MappingJacksonValue getVotes(@CookieValue(value = "access_token", required = false) String accessToken) {
        MappingJacksonValue value = new MappingJacksonValue(votes.values().stream().sorted(comparingLong(Vote::getAverage).reversed()).collect(toList()));
        if (StringUtils.isEmpty(accessToken)) {
            value.setSerializationView(Views.GuestView.class);
        } else {
            try {
                Jwt jwt = Jwts.parser().setSigningKey(JWT_PASSWORD).parse(accessToken);
                Claims claims = (Claims) jwt.getBody();
                String user = (String) claims.get("user");
                boolean isAdmin = Boolean.valueOf((String) claims.get("admin"));
                if ("Guest".equals(user) || !validUsers.contains(user)) {
                    value.setSerializationView(Views.GuestView.class);
                } else {
                    ((Collection<Vote>) value.getValue()).forEach(v -> v.setFlag(FLAGS.get(4)));
                    value.setSerializationView(isAdmin ? Views.AdminView.class : Views.UserView.class);
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
                if (validUsers.contains(user)) {
                    ofNullable(votes.get(title)).ifPresent(v -> v.incrementNumberOfVotes(totalVotes));
                    return ResponseEntity.accepted().build();
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }
}
