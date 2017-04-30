package org.owasp.webgoat.plugin.challenge5;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.owasp.webgoat.plugin.SolutionConstants.JWT_PASSWORD;

/**
 * @author nbaars
 * @since 4/23/17.
 */
@RestController
@RequestMapping("/votings")
public class Votings {

    @AllArgsConstructor
    @Getter
    private class Voting {
        @JsonView(Views.GuestView.class)
        private String title;
        @JsonView(Views.GuestView.class)
        private String information;
        @JsonView(Views.GuestView.class)
        private String imageSmall;
        @JsonView(Views.GuestView.class)
        private String imageBig;
        @JsonView(Views.UserView.class)
        private int numberOfVotes;
        @JsonView(Views.AdminView.class)
        private String flag;
    }

    private int totalVotes = 38929;
    private List votings = Lists.newArrayList(
            new Voting("Admin lost password",
                    "In this challenge you will need to help the admin and find the password in order to login",
                    "challenge1-small.png", "challenge1.png", 14242, null),
            new Voting("Vote for your favourite",
                    "In this challenge ...",
                    "challenge5-small.png", "challenge5.png", 12345, null),
            new Voting("Get is for free",
                    "The objective for this challenge is to buy a Samsung phone for free.",
                    "challenge2-small.png", "challenge2.png", 12342, null)
    );

    @GetMapping("/login")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void login(@RequestParam("user") String user, HttpServletResponse response) {
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
    }

    @GetMapping
    public MappingJacksonValue getVotings(@CookieValue(value = "access_token", required = false) String accessToken) {
        MappingJacksonValue value = new MappingJacksonValue(votings);
        if (accessToken == null) {
            value.setSerializationView(Views.GuestView.class);
        } else {
            value.setSerializationView(Views.UserView.class);
        }
        return value;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void vote(String title) {
        totalVotes = totalVotes + 1;
        //return
    }

    @GetMapping("/flags")
    @ResponseBody
    public ResponseEntity<?> getFlagInformation(@CookieValue("access_token") String accessToken, HttpServletResponse response) {
        return ResponseEntity.ok().build();
    }
}
