/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.missingac;

import static org.owasp.webgoat.lessons.missingac.MissingFunctionAC.PASSWORD_SALT_SIMPLE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DisplayUserTest {

  @Test
  void testDisplayUserCreation() {
    DisplayUser displayUser =
        new DisplayUser(new User("user1", "password1", true), PASSWORD_SALT_SIMPLE);
    Assertions.assertThat(displayUser.isAdmin()).isTrue();
  }

  @Test
  void testDisplayUserHash() {
    DisplayUser displayUser =
        new DisplayUser(new User("user1", "password1", false), PASSWORD_SALT_SIMPLE);
    Assertions.assertThat(displayUser.getUserHash())
        .isEqualTo("cplTjehjI/e5ajqTxWaXhU5NW9UotJfXj+gcbPvfWWc=");
  }
}
