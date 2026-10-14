/*
 * SPDX-FileCopyrightText: Copyright © 2008 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.lessons;

import lombok.Getter;

public enum Category {
  INTRODUCTION("category.introduction"),
  GENERAL("category.general"),

  A1("category.a1"),
  A2("category.a2"),
  A3("category.a3"),

  A5("category.a5"),
  A6("category.a6"),
  A7("category.a7"),
  A8("category.a8"),
  A9("category.a9"),
  A10("category.a10"),

  CLIENT_SIDE("category.client.side"),

  CHALLENGE("category.challenge");

  @Getter private String name;

  Category(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return getName();
  }
}
