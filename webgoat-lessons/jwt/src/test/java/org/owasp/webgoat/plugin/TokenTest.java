package org.owasp.webgoat.plugin;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TokenTest {

    @Test
    public void test() {
        String key = "qwertyqwerty1234";
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("username", "Jerry");
        claims.put("aud", "webgoat.org");
        claims.put("email", "jerry@webgoat.com");
        String token = Jwts.builder()
                .setHeaderParam("kid", "webgoat_key")
                .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, key).compact();
        System.out.println(token);
        Jwt jwt = Jwts.parser().setSigningKey("qwertyqwerty1234").parse(token);
        jwt = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter(){
            @Override
            public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                return TextCodec.BASE64.decode(key);
            }
        }).parse(token);

    }

    @Test
    public void testRefresh() {
        Instant now = Instant.now(); //current date
        Claims claims = Jwts.claims().setIssuedAt(Date.from(now.minus(Duration.ofDays(10))));
        claims.setExpiration(Date.from(now.minus(Duration.ofDays(9))));
        claims.put("admin", "false");
        claims.put("user", "Tom");
        String token = Jwts.builder().setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, "bm5n3SkxCX4kKRy4")
                .compact();
        //Jws<Claims> jws = Jwts.parser().setSigningKey("bm5n3SkxCX4kKRy4").parseClaimsJws(token);
        //Jwts.parser().setSigningKey().parsePlaintextJws(token);
        System.out.println(token);
    }
}
