/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.jwt.votes;

/**
 * @author nbaars
 * @since 4/30/17.
 */
public class Views {
  public interface GuestView {}

  public interface UserView extends GuestView {}
}
