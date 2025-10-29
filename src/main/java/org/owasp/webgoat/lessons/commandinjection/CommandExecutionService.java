/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutionService {

  public CommandExecutionResult execute(File workingDirectory, String command) {
    boolean isWindows = System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    String shell = isWindows ? "cmd.exe" : "/bin/sh";
    String switchArg = isWindows ? "/c" : "-c";

    ProcessBuilder builder = new ProcessBuilder(shell, switchArg, command);
    if (workingDirectory != null) {
      builder.directory(workingDirectory);
    }
    builder.redirectErrorStream(true);

    String processOutput = "";
    boolean timedOut = false;
    String executionError = null;

    try {
      Process process = builder.start();
      try (OutputStream os = process.getOutputStream()) {
        os.close();
      }
      boolean finished = process.waitFor(5, TimeUnit.SECONDS);
      if (!finished) {
        timedOut = true;
        process.destroyForcibly();
        process.waitFor(1, TimeUnit.SECONDS);
      }
      if (finished || !process.isAlive()) {
        try (InputStream in = process.getInputStream()) {
          processOutput = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
      } else {
        processOutput = "";
        process.getInputStream().close();
      }
    } catch (IOException | InterruptedException e) {
      executionError = e.getMessage();
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }

    return new CommandExecutionResult(command, processOutput, timedOut, executionError);
  }

  public record CommandExecutionResult(
      String command, String output, boolean timedOut, String executionError) {}
}
