/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import java.util.Arrays;
import java.util.Locale;

/**
 * Wording of the poisoned {@code total_invoices} tool. The variants range from an obvious command
 * to an instruction that reads like ordinary plumbing between two tools of the same server.
 */
public enum DescriptionVariant {
  /** An obvious, out-of-place command. Capable models usually refuse it. */
  BLUNT,
  /** Proof-of-concept wording: the token is "needed" for billing reconciliation. */
  TASK_SHAPED,
  /** The tool description is clean; the instruction hides in the parameter schema. */
  PARAMETER;

  public String id() {
    return name().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  public static DescriptionVariant fromId(String id) {
    return Arrays.stream(values())
        .filter(v -> v.id().equals(id))
        .findFirst()
        .orElse(TASK_SHAPED);
  }
}
