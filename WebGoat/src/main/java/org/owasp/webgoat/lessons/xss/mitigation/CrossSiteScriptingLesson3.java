/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xss.mitigation;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints(
    value = {
      "xss-mitigation-3-hint1",
      "xss-mitigation-3-hint2",
      "xss-mitigation-3-hint3",
      "xss-mitigation-3-hint4"
    })
public class CrossSiteScriptingLesson3 implements AssignmentEndpoint {

  @PostMapping("/CrossSiteScripting/attack3")
  @ResponseBody
  public AttackResult completed(@RequestParam String editor) {
    String unescapedString = org.jsoup.parser.Parser.unescapeEntities(editor, true);
    try {
      if (editor.isEmpty()) return failed(this).feedback("xss-mitigation-3-no-code").build();
      Document doc = Jsoup.parse(unescapedString);
      String[] lines = unescapedString.split("<html>");

      String include = (lines[0]);
      String fistNameElement =
          doc.select("body > table > tbody > tr:nth-child(1) > td:nth-child(2)").first().text();
      String lastNameElement =
          doc.select("body > table > tbody > tr:nth-child(2) > td:nth-child(2)").first().text();

      boolean includeCorrect = false;
      boolean firstNameCorrect = false;
      boolean lastNameCorrect = false;

      if (include.contains("<%@")
          && include.contains("taglib")
          && include.contains("uri=\"https://www.owasp.org/index.php/OWASP_Java_Encoder_Project\"")
          && include.contains("%>")) {
        includeCorrect = true;
      }
      if (fistNameElement.equals("${e:forHtml(param.first_name)}")) {
        firstNameCorrect = true;
      }
      if (lastNameElement.equals("${e:forHtml(param.last_name)}")) {
        lastNameCorrect = true;
      }

      if (includeCorrect && firstNameCorrect && lastNameCorrect) {
        return success(this).feedback("xss-mitigation-3-success").build();
      } else {
        return failed(this).feedback("xss-mitigation-3-failure").build();
      }
    } catch (Exception e) {
      return failed(this).output(e.getMessage()).build();
    }
  }
}
