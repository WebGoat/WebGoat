package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LabelAndHintTest extends IntegrationTest {


    @Test
    public void testSingleLabel() {
        Assertions.assertTrue(true);
        JsonPath jsonPath = RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Accept-Language","en")
                .cookie("JSESSIONID", getWebGoatCookie())
                .get(url("service/labels.mvc")).then().statusCode(200).extract().jsonPath();

        Assertions.assertEquals("Try again: but this time enter a value before hitting go.", jsonPath.getString("\'http-basics.close\'"));
    }

    @Test
    public void testLabels() {

        Properties propsDefault = getProperties("");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        checkLang(propsDefault,"nl");
        checkLang(propsDefault,"de");
        checkLang(propsDefault,"fr");
        checkLang(propsDefault,"ru");

    }

    private Properties getProperties(String lang) {
        Properties prop = null;
        if (lang == null || lang.equals("")) { lang = ""; } else { lang = "_"+lang; }
        try (InputStream input = new FileInputStream("src/main/resources/i18n/messages"+lang+".properties")) {

            prop = new Properties();
            // load a properties file
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    private void checkLang(Properties propsDefault, String lang) {
        JsonPath jsonPath = getLabels(lang);
        Properties propsLang = getProperties(lang);

        for (String key: propsLang.stringPropertyNames()) {
            if (!propsDefault.containsKey(key)) {
                System.err.println("key: " + key + " in (" +lang+") is missing from default properties");
                Assertions.fail();
            }
            /*if (!jsonPath.getString("\'"+key+"\'").equals(propsLang.get(key))) {
                System.out.println("key: " + key + " in (" +lang+") has incorrect translation in label service");
                System.out.println("actual:"+jsonPath.getString("\'"+key+"\'"));
                System.out.println("expected: "+propsLang.getProperty(key));
                System.out.println();
                //Assertions.fail();
            }*/
        }
    }

    private JsonPath getLabels(String lang) {
        return RestAssured.given()
                .when()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Accept-Language",lang)
                .cookie("JSESSIONID", getWebGoatCookie())
                //.log().headers()
                .get(url("service/labels.mvc"))
                .then()
                //.log().all()
                .statusCode(200).extract().jsonPath();
    }

}
