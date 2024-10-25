package org.owasp.webgoat;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class XSSIntegrationTest extends IntegrationTest {

  @Test
  public void crossSiteScriptingAssignments() {
    startLesson("CrossSiteScripting");

    Map<String, Object> params = new HashMap<>();
    params.clear();
    params.put("checkboxAttack1", "value");
    checkAssignment(url("CrossSiteScripting/attack1"), params, true);

    params.clear();
    params.put("QTY1", "1");
    params.put("QTY2", "1");
    params.put("QTY3", "1");
    params.put("QTY4", "1");
    params.put("field1", "<script>alert('XSS+Test')</script>");
    params.put("field2", "111");
    checkAssignmentWithGet(url("CrossSiteScripting/attack5a"), params, true);

    params.clear();
    params.put("DOMTestRoute", "start.mvc#test");
    checkAssignment(url("CrossSiteScripting/attack6a"), params, true);

    params.clear();
    params.put("param1", "42");
    params.put("param2", "24");

    String result =
        RestAssured.given()
            .when()
            .relaxedHTTPSValidation()
            .cookie("JSESSIONID", getWebGoatCookie())
            .header("webgoat-requested-by", "dom-xss-vuln")
            .header("X-Requested-With", "XMLHttpRequest")
            .formParams(params)
            .post(url("CrossSiteScripting/phone-home-xss"))
            .then()
            .statusCode(200)
            .extract()
            .path("output");
    String secretNumber = result.substring("phoneHome Response is ".length());

    params.clear();
    params.put("successMessage", secretNumber);
    checkAssignment(url("CrossSiteScripting/dom-follow-up"), params, true);

    params.clear();
    params.put(
        "question_0_solution",
        "Solution 4: No because the browser trusts the website if it is acknowledged trusted, then"
            + " the browser does not know that the script is malicious.");
    params.put(
        "question_1_solution",
        "Solution 3: The data is included in dynamic content that is sent to a web user without"
            + " being validated for malicious content.");
    params.put(
        "question_2_solution",
        "Solution 1: The script is permanently stored on the server and the victim gets the"
            + " malicious script when requesting information from the server.");
    params.put(
        "question_3_solution",
        "Solution 2: They reflect the injected script off the web server. That occurs when input"
            + " sent to the web server is part of the request.");
    params.put(
        "question_4_solution",
        "Solution 4: No there are many other ways. Like HTML, Flash or any other type of code that"
            + " the browser executes.");
    checkAssignment(url("CrossSiteScripting/quiz"), params, true);

    params.clear();
    params.put(
        "editor",
        "<%@ taglib uri=\"https://www.owasp.org/index.php/OWASP_Java_Encoder_Project\" %>"
            + "<html>"
            + "<head>"
            + "<title>Using GET and POST Method to Read Form Data</title>"
            + "</head>"
            + "<body>"
            + "<h1>Using POST Method to Read Form Data</h1>"
            + "<table>"
            + "<tbody>"
            + "<tr>"
            + "<td><b>First Name:</b></td>"
            + "<td>${e:forHtml(param.first_name)}</td>"
            + "</tr>"
            + "<tr>"
            + "<td><b>Last Name:</b></td>"
            + "<td>${e:forHtml(param.last_name)}</td>"
            + "</tr>"
            + "</tbody>"
            + "</table>"
            + "</body>"
            + "</html>");
    checkAssignment(url("CrossSiteScripting/attack3"), params, true);

    params.clear();
    params.put(
        "editor2",
        "Policy.getInstance(\"antisamy-slashdot.xml\");"
            + "Sammy s = new AntiSamy();"
            + "s.scan(newComment,\"\");"
            + "CleanResults();"
            + "MyCommentDAO.addComment(threadID, userID).getCleanHTML());");
    checkAssignment(url("CrossSiteScripting/attack4"), params, true);

    checkResults("CrossSiteScripting");
  }
}
