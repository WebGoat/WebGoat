/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.lessontemplate;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.List;
import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 1/5/17. */
@RestController
@AssignmentHints({"lesson-template.hints.1", "lesson-template.hints.2", "lesson-template.hints.3"})
public class SampleAttack implements AssignmentEndpoint {
  private static final String secretValue = "secr37Value";

  private final LessonSession userSessionData;

  public SampleAttack(LessonSession userSessionData) {
    this.userSessionData = userSessionData;
  }

  @PostMapping("/lesson-template/sample-attack")
  @ResponseBody
  public AttackResult completed(
      @RequestParam("param1") String param1, @RequestParam("param2") String param2) {
    if (userSessionData.getValue("some-value") != null) {
      // do any session updating you want here ... or not, just comment/example here
      // return failed().feedback("lesson-template.sample-attack.failure-2").build());
    }

    // overly simple example for success. See other existing lesssons for ways to detect 'success'
    // or 'failure'
    if (secretValue.equals(param1)) {
      return success(this)
          .output("Custom Output ...if you want, for success")
          .feedback("lesson-template.sample-attack.success")
          .build();
      // lesson-template.sample-attack.success is defined in
      // src/main/resources/i18n/WebGoatLabels.properties
    }

    // else
    return failed(this)
        .feedback("lesson-template.sample-attack.failure-2")
        .output(
            "Custom output for this failure scenario, usually html that will get rendered directly"
                + " ... yes, you can self-xss if you want")
        .build();
  }

  @GetMapping("lesson-template/shop/{user}")
  @ResponseBody
  public List<Item> getItemsInBasket(@PathVariable("user") String user) {
    return List.of(
        new Item("WG-1", "WebGoat promo", 12.0), new Item("WG-2", "WebGoat sticker", 0.00));
  }

  @AllArgsConstructor
  private class Item {
    private String number;
    private String description;
    private double price;
  }
}
