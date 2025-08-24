/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.missingac;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import lombok.Getter;

@Getter
public class DisplayUser {
  // intended to provide a display version of WebGoatUser for admins to view user attributes

  private final String username;
  private final boolean admin;
  private String userHash;

  public DisplayUser(User user, String passwordSalt) {
    this.username = user.getUsername();
    this.admin = user.isAdmin();

    try {
      this.userHash = genUserHash(user.getUsername(), user.getPassword(), passwordSalt);
    } catch (Exception ex) {
      this.userHash = "Error generating user hash";
    }
  }

  protected String genUserHash(String username, String password, String passwordSalt)
      throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    // salting is good, but static & too predictable ... short too for a salt
    String salted = password + passwordSalt + username;
    // md.update(salted.getBytes("UTF-8")); // Change this to "UTF-16" if needed
    byte[] hash = md.digest(salted.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(hash);
  }
}
