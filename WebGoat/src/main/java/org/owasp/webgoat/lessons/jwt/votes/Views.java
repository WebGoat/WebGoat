/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.jwt.votes;

public class Views {
  public interface GuestView {}

  public interface UserView extends GuestView {}
}
