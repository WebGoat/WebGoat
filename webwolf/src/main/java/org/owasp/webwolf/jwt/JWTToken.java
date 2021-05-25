package org.owasp.webwolf.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.jose4j.jwx.CompactSerializer.serialize;
import static org.springframework.util.Base64Utils.decodeFromUrlSafeString;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class JWTToken {

    private String encoded = "";
    private String secretKey;
    private String header;
    private boolean validHeader;
    private boolean validPayload;
    private boolean validToken;
    private String payload;
    private boolean signatureValid = true;

    public static JWTToken decode(String jwt, String secretKey) {
        var token = parseToken(jwt.trim().replace(System.getProperty("line.separator"), ""));
        return token.toBuilder().signatureValid(validateSignature(secretKey, jwt)).build();
    }

    private static Map<String, Object> parse(String header) {
        var reader = new ObjectMapper();
        try {
            return reader.readValue(header, TreeMap.class);
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    private static String write(String originalValue, Map<String, Object> data) {
        var writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        try {
            if (data.isEmpty()) {
                return originalValue;
            }
            return writer.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return originalValue;
        }
    }

    public static JWTToken encode(String header, String payloadAsString, String secretKey) {
        var headers = parse(header);
        var payload = parse(payloadAsString);

        var builder = JWTToken.builder()
                .header(write(header, headers))
                .payload(write(payloadAsString, payload))
                .validHeader(!hasText(header) || !headers.isEmpty())
                .validToken(true)
                .validPayload(!hasText(payloadAsString) || !payload.isEmpty());

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(payloadAsString);
        headers.forEach((k, v) -> jws.setHeader(k, v));
        if (!headers.isEmpty()) { //otherwise e30 meaning {} will be shown as header
            builder.encoded(serialize(new String[]{jws.getHeaders().getEncodedHeader(), jws.getEncodedPayload()}));
        }

        //Only sign when valid header and payload
        if (!headers.isEmpty() && !payload.isEmpty() && hasText(secretKey)) {
            jws.setDoKeyValidation(false);
            jws.setKey(new HmacKey(secretKey.getBytes(UTF_8)));
            try {
                builder.encoded(jws.getCompactSerialization());
                builder.signatureValid(true);
            } catch (JoseException e) {
                //Do nothing
            }
        }
        return builder.build();
    }

    private static JWTToken parseToken(String jwt) {
        var token = jwt.split("\\.");
        var builder = JWTToken.builder().encoded(jwt);

        if (token.length >= 2) {
            var header = new String(decodeFromUrlSafeString(token[0]), UTF_8);
            var payloadAsString = new String(decodeFromUrlSafeString(token[1]), UTF_8);
            var headers = parse(header);
            var payload = parse(payloadAsString);
            builder.header(write(header, headers));
            builder.payload(write(payloadAsString, payload));
            builder.validHeader(!headers.isEmpty());
            builder.validPayload(!payload.isEmpty());
            builder.validToken(!headers.isEmpty() && !payload.isEmpty());
        } else {
            builder.validToken(false);
        }
        return builder.build();
    }

    private static boolean validateSignature(String secretKey, String jwt) {
        if (hasText(secretKey)) {
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
