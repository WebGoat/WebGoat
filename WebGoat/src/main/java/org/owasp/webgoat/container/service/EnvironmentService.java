/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/environment")
@RequiredArgsConstructor
public class EnvironmentService {

  private final ApplicationContext context;

  @GetMapping("/server-directory")
  public String homeDirectory() {
    return context.getEnvironment().getProperty("webgoat.server.directory");
  }
}
