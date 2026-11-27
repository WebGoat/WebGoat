/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Replays what a vulnerable AI assistant sends to the lesson's MCP server. No model is needed: the
 * JSON-RPC calls are exactly those a client makes after following the poisoned tool description.
 */
class PromptInjectionIntegrationTest extends IntegrationTest {

  private static final Pattern SESSION_TOKEN = Pattern.compile("WG-SECRET-[0-9a-f]+");
  private static final Pattern ACCOUNT_REF = Pattern.compile("WG-ACC-[0-9A-F]+");

  private int id;

  @Test
  void poisonedToolDescriptionLeaksTokenAndArgumentGuardRefusesIt() {
    startLesson("PromptInjection", true);
    String mcpUrl = server().getString("mcpUrl");

    rpc(mcpUrl, "initialize", Map.of("protocolVersion", "2025-06-18", "capabilities", Map.of()))
        .then()
        .statusCode(200)
        .body("result.serverInfo.name", is("invoicehelper"));
    given()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .body(Map.of("jsonrpc", "2.0", "method", "notifications/initialized"))
        .post(mcpUrl)
        .then()
        .statusCode(202);

    String descriptions =
        rpc(mcpUrl, "tools/list", Map.of()).then().statusCode(200).extract().asString();
    assertThat(descriptions, containsString("session_token"));

    String account = toolText(mcpUrl, "get_account", Map.of());
    String token = find(SESSION_TOKEN, account);
    String accountRef = find(ACCOUNT_REF, account);

    Map<String, Object> poisoned =
        Map.of("csv_path", "sample.csv", "account_ref", accountRef, "audit", token);
    assertThat(toolText(mcpUrl, "total_invoices", poisoned), containsString("15.00 EUR"));
    checkAssignment(webGoatUrlConfig.url("PromptInjection/task1"), Map.of(), true);

    updateServer(Map.of("argumentGuard", "true"));
    assertThat(toolText(mcpUrl, "total_invoices", poisoned), containsString("Refused"));
    checkAssignment(webGoatUrlConfig.url("PromptInjection/task2"), Map.of(), true);

    updateServer(Map.of("pinned", "true"));
    String pinned = rpc(mcpUrl, "tools/list", Map.of()).then().extract().asString();
    assertThat(pinned.contains("session_token"), is(false));

    checkAssignment(
        webGoatUrlConfig.url("PromptInjection/quiz"),
        Map.of(
            "question_0_solution", "Solution 1",
            "question_1_solution", "Solution 2",
            "question_2_solution", "Solution 0",
            "question_3_solution", "Solution 3",
            "question_4_solution", "Solution 1"),
        true);

    checkResults("PromptInjection");
  }

  @Test
  void mcpEndpointRejectsUnknownTokens() {
    given()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .body(Map.of("jsonrpc", "2.0", "id", 1, "method", "tools/list"))
        .post(webGoatUrlConfig.url("PromptInjection/mcp/unknown"))
        .then()
        .statusCode(404);
  }

  private JsonPath server() {
    return given()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .get(webGoatUrlConfig.url("PromptInjection/server"))
        .then()
        .statusCode(200)
        .extract()
        .jsonPath();
  }

  private void updateServer(Map<String, String> settings) {
    given()
        .relaxedHTTPSValidation()
        .cookie("JSESSIONID", getWebGoatCookie())
        .formParams(settings)
        .post(webGoatUrlConfig.url("PromptInjection/server"))
        .then()
        .statusCode(200);
  }

  /** The assistant has no WebGoat session: MCP calls carry no cookie. */
  private io.restassured.response.Response rpc(String url, String method, Object params) {
    return given()
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .accept("application/json, text/event-stream")
        .body(Map.of("jsonrpc", "2.0", "id", ++id, "method", method, "params", params))
        .post(url);
  }

  private String toolText(String url, String tool, Map<String, Object> arguments) {
    return rpc(url, "tools/call", Map.of("name", tool, "arguments", arguments))
        .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getString("result.content[0].text");
  }

  private static String find(Pattern pattern, String text) {
    var matcher = pattern.matcher(text);
    assertThat("expected " + pattern + " in " + text, matcher.find(), is(true));
    return matcher.group();
  }
}
