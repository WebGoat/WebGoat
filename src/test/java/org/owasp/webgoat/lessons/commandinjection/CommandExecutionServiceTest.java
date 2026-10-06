/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class CommandExecutionServiceTest {

  private final CommandExecutionService service = new CommandExecutionService();

  @Test
  void shouldDrainAndLimitLargeOutput() {
    String command =
        CommandExecutionService.isWindows()
            ? "for /L %i in (1,1,10000) do @echo 01234567890123456789"
            : "yes 01234567890123456789 | head -c 100000";

    var result = service.execute(null, command);

    assertThat(result.timedOut()).isFalse();
    assertThat(result.outputTruncated()).isTrue();
    assertThat(result.output()).endsWith("[Output truncated]");
  }

  @Test
  void shouldStopACommandAndItsChildAtTheTimeout() {
    assumeFalse(CommandExecutionService.isWindows());

    long startedAt = System.nanoTime();
    var result = service.execute(null, "sleep 30 & wait");
    Duration elapsed = Duration.ofNanos(System.nanoTime() - startedAt);

    assertThat(result.timedOut()).isTrue();
    assertThat(elapsed).isLessThan(Duration.ofSeconds(8));
  }
}
