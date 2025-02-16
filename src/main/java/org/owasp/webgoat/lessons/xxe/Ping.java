/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xxe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.CurrentUsername;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class Ping {

  @Value("${webgoat.user.directory}")
  private String webGoatHomeDirectory;

  @GetMapping
  @ResponseBody
  public String logRequest(
      @RequestHeader("User-Agent") String userAgent,
      @RequestParam(required = false) String text,
      @CurrentUsername String username) {
    String logLine = String.format("%s %s %s", "GET", userAgent, text);
    log.debug(logLine);
    File logFile = new File(webGoatHomeDirectory, "/XXE/log" + username + ".txt");
    try {
      try (PrintWriter pw = new PrintWriter(logFile)) {
        pw.println(logLine);
      }
    } catch (FileNotFoundException e) {
      log.error("Error occurred while writing the logfile", e);
    }
    return "";
  }
}
