package org.owasp.webwolf.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class JWTToken {

    private static final Pattern jwtPattern = Pattern.compile("(.*)\\.(.*)\\.(.*)");

    private String encoded = "";
    private String secretKey;
    private String header;
    private String payload;
    private boolean signatureValid = true;
    private boolean validToken = true;

    public void decode() {
        validToken = parseToken(encoded);
        signatureValid = validateSignature(secretKey, encoded);
    }

    public void encode() {
        var mapper = new ObjectMapper();
        try {
            if (StringUtils.hasText(secretKey)) {
                encoded = Jwts.builder()
                        .signWith(SignatureAlgorithm.HS256, Base64Utils.encodeToUrlSafeString(secretKey.getBytes()))
                        .setClaims(mapper.readValue(payload, Map.class))
                        .setHeader(mapper.readValue(header, Map.class))
                        .compact();
            } else {
                encoded = Jwts.builder()
                        .setClaims(mapper.readValue(payload, Map.class))
                        .setHeader(mapper.readValue(header, Map.class))
                        .compact();
            }
            validToken = true;
        } catch (JsonProcessingException e) {
            validToken = false;
            signatureValid = false;
        }
    }

    private boolean parseToken(String jwt) {
        var matcher = jwtPattern.matcher(jwt);
        var mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
        if (matcher.matches()) {
            try {
                Jwt<Header, Claims> headerClaimsJwt = Jwts.parser().parseClaimsJwt(matcher.group(1) + "." + matcher.group(2) + ".");
                this.header = mapper.writeValueAsString(headerClaimsJwt.getHeader());
                this.payload = mapper.writeValueAsString(headerClaimsJwt.getBody());
            } catch (Exception e) {
                try {
                    this.header = mapper.writeValueAsString(new String(Base64Utils.decodeFromUrlSafeString(matcher.group(1))));
                    this.payload = mapper.writeValueAsString(new String(Base64Utils.decodeFromUrlSafeString(matcher.group(2))));
                } catch (Exception ex) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean validateSignature(String secretKey, String jwt) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
