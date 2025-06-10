/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.idor;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "idor.hints.otherProfile1",
  "idor.hints.otherProfile2",
  "idor.hints.otherProfile3",
  "idor.hints.otherProfile4",
  "idor.hints.otherProfile5",
  "idor.hints.otherProfile6",
  "idor.hints.otherProfile7",
  "idor.hints.otherProfile8",
  "idor.hints.otherProfile9"
})
public class IDORViewOtherProfile implements AssignmentEndpoint {

  private final LessonSession userSessionData;

  public IDORViewOtherProfile(LessonSession userSessionData) {
    this.userSessionData = userSessionData;
  }

  @GetMapping(
      path = "/IDOR/profile/{userId}",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(@PathVariable("userId") String userId) {

    Object obj = userSessionData.getValue("idor-authenticated-as");
    if (obj != null && obj.equals("tom")) {
      // going to use session auth to view this one
      String authUserId = (String) userSessionData.getValue("idor-authenticated-user-id");
      if (userId != null && !userId.equals(authUserId)) {
        // on the right track
        UserProfile requestedProfile = new UserProfile(userId);
        // secure code would ensure there was a horizontal access control check prior to dishing up
        // the requested profile
        if (requestedProfile.getUserId() != null
            && requestedProfile.getUserId().equals("2342388")) {
          return success(this)
              .feedback("idor.view.profile.success")
              .output(requestedProfile.profileToMap().toString())
              .build();
        } else {
          return failed(this).feedback("idor.view.profile.close1").build();
        }
      } else {
        return failed(this).feedback("idor.view.profile.close2").build();
      }
    }
    return failed(this).build();
  }
}
