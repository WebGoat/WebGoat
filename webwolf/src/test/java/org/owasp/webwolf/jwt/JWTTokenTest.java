package org.owasp.webwolf.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JWTTokenTest {

    @Test
    void encodeCorrectTokenWithoutSignature() {
        var headers = Map.of("alg", "HS256", "typ", "JWT");
        var payload = Map.of("test", "test");
        var token = JWTToken.builder().header(toString(headers)).payload(toString(payload)).build();

        token.encode();

        assertThat(token.getEncoded()).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJ0ZXN0IjoidGVzdCJ9");
    }

    @Test
    void encodeCorrectTokenWithSignature() {
        var headers = Map.of("alg", "HS256", "typ", "JWT");
        var payload = Map.of("test", "test");
        var token = JWTToken.builder()
                .header(toString(headers))
                .payload(toString(payload))
                .secretKey("test")
                .build();

        token.encode();

        assertThat(token.getEncoded()).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJ0ZXN0IjoidGVzdCJ9.KOobRHDYyaesV_doOk11XXGKSONwzllraAaqqM4VFE4");
    }

    @Test
    void encodeTokenWithNonJsonInput() {
        var token = JWTToken.builder()
                .header("aaa")
                .payload("bbb")
                .secretKey("test")
                .build();

        token.encode();

        assertThat(token.getEncoded()).isEqualTo("eyJhbGciOiJIUzI1NiJ9.YmJi.VAcRegquayARuahZZ1ednXpbAyv7KEFnyjNJlxLNX0I");
    }

    @Test
    void decodeValidSignedToken() {
        var token = JWTToken.builder()
                .encoded("eyJhbGciOiJIUzI1NiJ9.eyJ0ZXN0IjoidGVzdCJ9.KOobRHDYyaesV_doOk11XXGKSONwzllraAaqqM4VFE4")
                .secretKey("test")
                .build();

        token.decode();

        assertThat(token.getHeader()).contains("\"alg\" : \"HS256\"");
        assertThat(token.isSignatureValid()).isTrue();
    }

    @Test
    void decodeInvalidSignedToken() {
        var token = JWTToken.builder().encoded("eyJhbGciOiJIUzI1NiJ9.eyJ0ZXsdfdfsaasfddfasN0IjoidGVzdCJ9.KOobRHDYyaesV_doOk11XXGKSONwzllraAaqqM4VFE4").build();

        token.decode();

        assertThat(token.getHeader()).contains("\"alg\":\"HS256\"");
        assertThat(token.getPayload()).contains("{\"te");
    }

    @SneakyThrows
    private String toString(Map<String, String> map) {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

}