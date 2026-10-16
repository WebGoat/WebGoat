/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xxe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class XXETestPayload {

  private XXETestPayload() {}

  static String readKnownFile(Path directory) throws IOException {
    Path target = Files.writeString(directory.resolve("xxe.txt"), "etc Windows");

    return """
        <?xml version="1.0" standalone="yes" ?>
        <!DOCTYPE user [<!ENTITY root SYSTEM "%s">]>
        <comment><text>&root;</text></comment>
        """
        .formatted(target.toUri());
  }
}
