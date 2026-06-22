/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.vulnerablecomponents;

public interface Contact {

  public Integer getId();

  public void setId(Integer id);

  public String getFirstName();

  public void setFirstName(String firstName);

  public String getLastName();

  public void setLastName(String lastName);

  public String getEmail();

  public void setEmail(String email);
}
