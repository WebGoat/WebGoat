/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.xxe;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"xxe.hints.content.type.xxe.1", "xxe.hints.content.type.xxe.2"})
public class ContentTypeAssignment extends AssignmentEndpoint {

  private static final String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "etc", "var"};
  private static final String[] DEFAULT_WINDOWS_DIRECTORIES = {
    "Windows", "Program Files (x86)", "Program Files", "pagefile.sys"
  };

  @Value("${webgoat.server.directory}")
  private String webGoatHomeDirectory;

  private final CommentsCache comments;

  public ContentTypeAssignment(CommentsCache comments) {
    this.comments = comments;
  }

  @PostMapping(path = "xxe/content-type")
  @ResponseBody
  public AttackResult createNewUser(
      @RequestBody String commentStr,
      @RequestHeader("Content-Type") String contentType,
      @CurrentUser WebGoatUser user) {
    AttackResult attackResult = failed(this).build();

    if (APPLICATION_JSON_VALUE.equals(contentType)) {
      parseJson(commentStr).ifPresent(c -> comments.addComment(c, user, true));
      attackResult = failed(this).feedback("xxe.content.type.feedback.json").build();
    }

    if (null != contentType && contentType.contains(MediaType.APPLICATION_XML_VALUE)) {
      try {
        Comment comment = comments.parseXml(commentStr, false);
        comments.addComment(comment, user, false);
        if (checkSolution(comment)) {
          attackResult = success(this).build();
        }
      } catch (Exception e) {
        String error = ExceptionUtils.getStackTrace(e);
        attackResult = failed(this).feedback("xxe.content.type.feedback.xml").output(error).build();
      }
    }

    return attackResult;
  }

  protected Optional<Comment> parseJson(String comment) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return of(mapper.readValue(comment, Comment.class));
    } catch (IOException e) {
      return empty();
    }
  }

  private boolean checkSolution(Comment comment) {
    String[] directoriesToCheck =
        OS.isFamilyMac() || OS.isFamilyUnix()
            ? DEFAULT_LINUX_DIRECTORIES
            : DEFAULT_WINDOWS_DIRECTORIES;
    boolean success = false;
    for (String directory : directoriesToCheck) {
      success |= org.apache.commons.lang3.StringUtils.contains(comment.getText(), directory);
    }
    return success;
  }
}
