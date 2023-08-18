package jwt_test.jwt_test_1;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class App
{

    private static void bad1() {
        // ruleid: jjwt-none-alg
        String jws = Jwts.builder()
                .setSubject("Bob")
                .compact();
    }

    private static void ok1() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // ok: jjwt-none-alg
        String jws = Jwts.builder()
                .setSubject("Bob")
                .signWith(key)
                .compact();
    }

    public static void main( String[] args )
    {
        bad1();
        ok1();
    }
}
