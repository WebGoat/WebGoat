package org.owasp.webgoat.container.lessons;

import lombok.Getter;

/**
 * *************************************************************************************************
 *
 * <p>
 *
 * <p>This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * <p>Copyright (c) 2002 - 2014 Bruce Mayhew
 *
 * <p>This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <p>Getting Source ==============
 *
 * <p>Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 28, 2003
 */
public enum Category {
  INTRODUCTION("Introduction"),
  GENERAL("General"),

  A1("(A1) Broken Access Control"),
  A2("(A2) Cryptographic Failures"),
  A3("(A3) Injection"),

  A5("(A5) Security Misconfiguration"),
  A6("(A6) Vuln & Outdated Components"),
  A7("(A7) Identity & Auth Failure"),
  A8("(A8) Software & Data Integrity"),
  A9("(A9) Security Logging Failures"),
  A10("(A10) Server-side Request Forgery"),

  CLIENT_SIDE("Client side"),

  CHALLENGE("Challenges");

  @Getter private String name;

  Category(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return getName();
  }
}
