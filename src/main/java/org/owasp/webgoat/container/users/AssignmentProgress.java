/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.owasp.webgoat.container.lessons.Assignment;
import org.springframework.util.Assert;

@Entity
@EqualsAndHashCode
public class AssignmentProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @OneToOne(cascade = CascadeType.ALL)
  private Assignment assignment;

  @Getter private boolean solved;

  protected AssignmentProgress() {}

  public AssignmentProgress(Assignment assignment) {
    this.assignment = assignment;
  }

  public boolean hasSameName(String name) {
    Assert.notNull(name, "Name cannot be null");

    return assignment.getName().equals(name);
  }

  public void solved() {
    this.solved = true;
  }

  public void reset() {
    this.solved = false;
  }
}
