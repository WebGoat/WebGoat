package org.owasp.webwolf.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.Base64Utils.decodeFromUrlSafeString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JWTToken {

    private static final Pattern jwtPattern = Pattern.compile("(.*)\\.(.*)\\.(.*)");

    private String encoded = "";
    private String secretKey;
    private String header;
    private String payload;
    private boolean signatureValid = true;

    public void decode() {
        parseToken(encoded.trim().replace(System.getProperty("line.separator"), ""));
        signatureValid = validateSignature(secretKey, encoded);
    }

    public void encode() {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(payload);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setDoKeyValidation(false);
        if (StringUtils.hasText(secretKey)) {
            jws.setKey(new HmacKey(secretKey.getBytes(UTF_8)));
            try {
                encoded = jws.getCompactSerialization();
                signatureValid = true;
            } catch (JoseException e) {
                header = "";
                payload = "";
            }
        } else {
            var encodedHeader = jws.getHeaders().getEncodedHeader();
            var encodedPayload = jws.getEncodedPayload();
            encoded = CompactSerializer.serialize(new String[]{encodedHeader, encodedPayload});
        }
    }

    private boolean parseToken(String jwt) {
        var matcher = jwtPattern.matcher(jwt);
        var mapper = new ObjectMapper();

        if (matcher.matches()) {
            try {
                var prettyPrint = mapper.writerWithDefaultPrettyPrinter();
                this.header = prettyPrint.writeValueAsString(mapper.readValue(decodeFromUrlSafeString(matcher.group(1)), Map.class));
                this.payload = prettyPrint.writeValueAsString(mapper.readValue(decodeFromUrlSafeString(matcher.group(2)), Map.class));
                return true;
            } catch (Exception e) {
                this.header = new String(decodeFromUrlSafeString(matcher.group(1)));
                this.payload = new String(decodeFromUrlSafeString(matcher.group(2)));
                return false;
            }
        } else {
            this.header = "error";
            this.payload = "error";
        }
        return false;
    }

    private boolean validateSignature(String secretKey, String jwt) {
        if (StringUtils.hasText(secretKey)) {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setVerificationKey(new HmacKey(secretKey.getBytes(UTF_8)))
                    .setRelaxVerificationKeyValidation()
                    .build();
            try {
                jwtConsumer.processToClaims(jwt);
                return true;
            } catch (InvalidJwtException e) {
                return false;
            }
        }
        return false;
    }

}
