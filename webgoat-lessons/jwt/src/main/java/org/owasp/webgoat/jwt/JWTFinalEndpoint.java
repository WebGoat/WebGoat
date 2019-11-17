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
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
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
@RestController
@AssignmentHints({"jwt-final-hint1", "jwt-final-hint2", "jwt-final-hint3", "jwt-final-hint4", "jwt-final-hint5", "jwt-final-hint6"})
public class JWTFinalEndpoint extends AssignmentEndpoint {

    private final DataSource dataSource;

    private JWTFinalEndpoint(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/JWT/final/follow/{user}")
    public @ResponseBody
    String follow(@PathVariable("user") String user) {
        if ("Jerry".equals(user)) {
            return "Following yourself seems redundant";
        } else {
            return "You are now following Tom";
        }
    }

    @PostMapping("/JWT/final/delete")
    public @ResponseBody
    AttackResult resetVotes(@RequestParam("token") String token) {
        if (StringUtils.isEmpty(token)) {
            return failed(this).feedback("jwt-invalid-token").build();
        } else {
            try {
                final String[] errorMessage = {null};
                Jwt jwt = Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                        final String kid = (String) header.get("kid");
                        try (var connection = dataSource.getConnection()) {
                            ResultSet rs = connection.createStatement().executeQuery("SELECT key FROM jwt_keys WHERE id = '" + kid + "'");
                            while (rs.next()) {
                                return TextCodec.BASE64.decode(rs.getString(1));
                            }
                        } catch (SQLException e) {
                            errorMessage[0] = e.getMessage();
                        }
                        return null;
                    }
                }).parseClaimsJws(token);
                if (errorMessage[0] != null) {
                    return failed(this).output(errorMessage[0]).build();
                }
                Claims claims = (Claims) jwt.getBody();
                String username = (String) claims.get("username");
                if ("Jerry".equals(username)) {
                    return failed(this).feedback("jwt-final-jerry-account").build();
                }
                if ("Tom".equals(username)) {
                    return success(this).build();
                } else {
                    return failed(this).feedback("jwt-final-not-tom").build();
                }
            } catch (JwtException e) {
                return failed(this).feedback("jwt-invalid-token").output(e.toString()).build();
            }
        }
    }
}
