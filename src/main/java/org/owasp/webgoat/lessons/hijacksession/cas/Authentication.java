/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.hijacksession.cas;

import java.security.Principal;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Angel Olle Blazquez
 */
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
