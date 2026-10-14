/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.lessons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Entity
public class Assignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String path;

  @Transient private List<String> hints;

  protected Assignment() {
    // Hibernate
  }

  public Assignment(String name) {
    this(name, name, new ArrayList<>());
  }

  public Assignment(String name, String path, List<String> hints) {
    if (path.equals("") || path.equals("/") || path.equals("/WebGoat/")) {
      throw new IllegalStateException(
          "The path of assignment '"
              + name
              + "' overrides WebGoat endpoints, please choose a path within the scope of the"
              + " lesson");
    }
    this.name = name;
    this.path = path;
    this.hints = hints;
  }
}
