/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.clientsidefiltering;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class SalariesTest {

  @TempDir Path tempDirectory;

  @Test
  void rejectsExternalEntityDeclarations() throws Exception {
    Path salariesDirectory = Files.createDirectories(tempDirectory.resolve("ClientSideFiltering"));
    Files.writeString(
        salariesDirectory.resolve("employees.xml"),
        """
        <!DOCTYPE Employees [
          <!ENTITY secret SYSTEM "file:///etc/passwd">
        ]>
        <Employees><Employee><SSN>&secret;</SSN></Employee></Employees>
        """);

    var salaries = new Salaries();
    ReflectionTestUtils.setField(salaries, "webGoatHomeDirectory", tempDirectory.toString());

    assertThat(salaries.invoke()).isEmpty();
  }
}
