/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges;

public interface SolutionConstants {

  // Password is generated at runtime via Assignment class
  String PASSWORD = System.getenv().getOrDefault("WEBGOAT_ADMIN_PASSWORD", java.util.UUID.randomUUID().toString());
}
