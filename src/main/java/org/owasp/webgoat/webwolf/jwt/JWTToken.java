/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StringUtils.hasText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.keys.HmacKey;
import org.jose4j.keys.resolvers.VerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UnresolvableKeyException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class JWTToken {

  private String encoded;
  private String secretKey;
  private String header;
  private boolean validHeader;
  private boolean validPayload;
  private boolean validToken;
  private String payload;
  private boolean signatureValid = true;

  public static JWTToken decode(String jwt, String secretKey) {
    return decode(jwt, secretKey, null);
  }

  public static JWTToken decode(String jwt, String secretKey, String jwksJson) {
    var cleanedToken = jwt.trim().replace(System.getProperty("line.separator"), "");
    var token = parseToken(cleanedToken);
    return token
        .toBuilder()
        .signatureValid(validateSignature(secretKey, jwksJson, cleanedToken))
        .build();
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

    var builder =
        JWTToken.builder()
            .header(write(header, headers))
            .payload(write(payloadAsString, payload))
            .validHeader(!hasText(header) || !headers.isEmpty())
            .validToken(true)
            .validPayload(!hasText(payloadAsString) || !payload.isEmpty());

    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(payloadAsString);
    headers.forEach((k, v) -> jws.setHeader(k, v));
    if (!headers.isEmpty()) { // otherwise e30 meaning {} will be shown as header
      builder.encoded(
          CompactSerializer.serialize(
              new String[] {jws.getHeaders().getEncodedHeader(), jws.getEncodedPayload()}));
    }

    // Only sign when valid header and payload
    if (!headers.isEmpty() && !payload.isEmpty() && hasText(secretKey)) {
      jws.setDoKeyValidation(false);
      jws.setKey(new HmacKey(secretKey.getBytes(UTF_8)));
      try {
        builder.encoded(jws.getCompactSerialization());
        builder.signatureValid(true);
      } catch (JoseException e) {
        // Do nothing
      }
    }
    return builder.build();
  }

  private static JWTToken parseToken(String jwt) {
    var token = jwt.split("\\.");
    var builder = JWTToken.builder().encoded(jwt);

    if (token.length >= 2) {
      var header = new String(Base64.getUrlDecoder().decode(token[0]), UTF_8);
      var payloadAsString = new String(Base64.getUrlDecoder().decode(token[1]), UTF_8);
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

  private static boolean validateSignature(String secretKey, String jwksJson, String jwt) {
    if (hasText(secretKey)) {
      return validateWithSharedSecret(secretKey, jwt);
    }
    if (hasText(jwksJson)) {
      return validateWithJwks(jwksJson, jwt);
    }
    return false;
  }

  private static boolean validateWithSharedSecret(String secretKey, String jwt) {
    JwtConsumer jwtConsumer =
        new JwtConsumerBuilder()
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

  private static boolean validateWithJwks(String jwksJson, String jwt) {
    try {
      JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(jwksJson);
      VerificationKeyResolver resolver =
          (JsonWebSignature jws, List<JsonWebStructure> nestingContext) -> {
            String keyId = jws.getKeyIdHeaderValue();
            if (hasText(keyId)) {
              for (JsonWebKey jwk : jsonWebKeySet.getJsonWebKeys()) {
                if (keyId.equals(jwk.getKeyId())) {
                  return jwk.getKey();
                }
              }
            }
            if (!jsonWebKeySet.getJsonWebKeys().isEmpty()) {
              return jsonWebKeySet.getJsonWebKeys().get(0).getKey();
            }
            throw new UnresolvableKeyException("No keys available in JWKS");
          };

      JwtConsumer jwtConsumer =
          new JwtConsumerBuilder()
              .setSkipAllValidators()
              .setVerificationKeyResolver(resolver)
              .setRelaxVerificationKeyValidation()
              .build();
      jwtConsumer.processToClaims(jwt);
      return true;
    } catch (JoseException | InvalidJwtException e) {
      return false;
    }
  }
}
