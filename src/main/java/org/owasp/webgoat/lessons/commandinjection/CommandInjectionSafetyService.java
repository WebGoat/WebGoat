/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommandInjectionSafetyService implements Initializable {

  private final Set<String> acknowledgedUsers = ConcurrentHashMap.newKeySet();

  public void acknowledge(WebGoatUser user) {
    acknowledgedUsers.add(user.getUsername());
  }

  public void requireAcknowledgement(WebGoatUser user) {
    if (!acknowledgedUsers.contains(user.getUsername())) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "commandinjection.safety.required");
    }
  }

  public boolean isAcknowledged(WebGoatUser user) {
    return acknowledgedUsers.contains(user.getUsername());
  }

  @Override
  public void initialize(WebGoatUser user) {
    acknowledgedUsers.remove(user.getUsername());
  }
}
