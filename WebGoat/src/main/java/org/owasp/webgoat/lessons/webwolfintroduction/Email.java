/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webwolfintroduction;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Email implements Serializable {

  private String contents;
  private String sender;
  private String title;
  private String recipient;
}
