package org.owasp.webgoat.plugin;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Test;

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
}
