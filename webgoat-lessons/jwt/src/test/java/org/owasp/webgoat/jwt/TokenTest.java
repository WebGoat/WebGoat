/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TokenTest {

    @Test
    public void test() {
        String key = "qwertyqwerty1234";
        Map<String, Object> claims = Map.of("username", "Jerry", "aud", "webgoat.org", "email", "jerry@webgoat.com");
        String token = Jwts.builder()
                .setHeaderParam("kid", "webgoat_key")
                .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
                .setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, key).compact();
        System.out.println(token);
        Jwt jwt = Jwts.parser().setSigningKey("qwertyqwerty1234").parse(token);
        jwt = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
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
