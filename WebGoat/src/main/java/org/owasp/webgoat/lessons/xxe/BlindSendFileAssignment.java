/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xxe;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AssignmentHints({
  "xxe.blind.hints.1",
  "xxe.blind.hints.2",
  "xxe.blind.hints.3",
  "xxe.blind.hints.4",
  "xxe.blind.hints.5"
})
public class BlindSendFileAssignment implements AssignmentEndpoint, Initializable {

  private final String webGoatHomeDirectory;
  private final CommentsCache comments;
  private final Map<WebGoatUser, String> userToFileContents = new HashMap<>();

  public BlindSendFileAssignment(
      @Value("${webgoat.user.directory}") String webGoatHomeDirectory, CommentsCache comments) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
    this.comments = comments;
  }

  private void createSecretFileWithRandomContents(WebGoatUser user) {
    var fileContents = "WebGoat 8.0 rocks... (" + randomAlphabetic(10) + ")";
    userToFileContents.put(user, fileContents);
    File targetDirectory = new File(webGoatHomeDirectory, "/XXE/" + user.getUsername());
    if (!targetDirectory.exists()) {
      targetDirectory.mkdirs();
    }
    try {
      Files.writeString(new File(targetDirectory, "secret.txt").toPath(), fileContents, UTF_8);
    } catch (IOException e) {
      log.error("Unable to write 'secret.txt' to '{}", targetDirectory);
    }
  }

  @PostMapping(path = "xxe/blind", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult addComment(
      @RequestBody String commentStr, @AuthenticationPrincipal WebGoatUser user) {
    var fileContentsForUser = userToFileContents.getOrDefault(user, "");

    // Solution is posted by the user as a separate comment
    if (commentStr.contains(fileContentsForUser)) {
      return success(this).build();
    }

    try {
      Comment comment = comments.parseXml(commentStr, false);
      if (fileContentsForUser.contains(comment.getText())) {
        comment.setText("Nice try, you need to send the file to WebWolf");
      }
      comments.addComment(comment, user, false);
    } catch (Exception e) {
      return failed(this).output(e.toString()).build();
    }
    return failed(this).build();
  }

  @Override
  public void initialize(WebGoatUser user) {
    comments.reset(user);
    userToFileContents.remove(user);
    createSecretFileWithRandomContents(user);
  }
}
