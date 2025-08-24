/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.hijacksession.cas;

import java.security.Principal;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Authentication implements Principal {

  private boolean authenticated = false;
  private String name;
  private Object credentials;
  private String id;

  @Builder
  public Authentication(String name, Object credentials, String id) {
    this.name = name;
    this.credentials = credentials;
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  protected void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  protected void setId(String id) {
    this.id = id;
  }
}
