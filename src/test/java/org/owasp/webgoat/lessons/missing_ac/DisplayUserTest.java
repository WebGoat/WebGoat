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

package org.owasp.webgoat.lessons.missing_ac;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.missing_ac.DisplayUser;
import org.owasp.webgoat.lessons.missing_ac.User;

import static org.owasp.webgoat.lessons.missing_ac.MissingFunctionAC.PASSWORD_SALT_SIMPLE;

class DisplayUserTest {

    @Test
    void testDisplayUserCreation() {
        DisplayUser displayUser = new DisplayUser(new User("user1", "password1", true), PASSWORD_SALT_SIMPLE);
        Assertions.assertThat(displayUser.isAdmin()).isTrue();
    }

    @Test
    void testDisplayUserHash() {
        DisplayUser displayUser = new DisplayUser(new User("user1", "password1", false), PASSWORD_SALT_SIMPLE);
        Assertions.assertThat(displayUser.getUserHash()).isEqualTo("cplTjehjI/e5ajqTxWaXhU5NW9UotJfXj+gcbPvfWWc=");
    }
}
