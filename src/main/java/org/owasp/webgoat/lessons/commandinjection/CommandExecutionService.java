/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutionService {

  private static final int MAX_OUTPUT_BYTES = 64 * 1024;
  private static final long PROCESS_TIMEOUT_SECONDS = 5;
  private static final long OUTPUT_DRAIN_TIMEOUT_SECONDS = 1;

  public CommandExecutionResult execute(File workingDirectory, String command) {
    return execute(workingDirectory, command, Map.of());
  }

  public CommandExecutionResult execute(
      File workingDirectory, String command, Map<String, String> environment) {
    String shell = isWindows() ? "cmd.exe" : "/bin/sh";
    String switchArg = isWindows() ? "/c" : "-c";
    return execute(
        workingDirectory, command, new ProcessBuilder(shell, switchArg, command), environment);
  }

  public CommandExecutionResult executeDirect(
      File workingDirectory, List<String> command, Map<String, String> environment) {
    if (command.isEmpty()) {
      return new CommandExecutionResult("", "", false, "No command provided", null, false);
    }
    return execute(
        workingDirectory, String.join(" ", command), new ProcessBuilder(command), environment);
  }

  static boolean isWindows() {
    return System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
  }

  private CommandExecutionResult execute(
      File workingDirectory,
      String displayCommand,
      ProcessBuilder builder,
      Map<String, String> environment) {
    if (workingDirectory != null) {
      builder.directory(workingDirectory);
    }
    builder.redirectErrorStream(true);
    builder.environment().putAll(environment);

    boolean timedOut = false;
    String executionError = null;
    Integer exitCode = null;
    BoundedOutputCollector outputCollector = null;
    Thread outputReader = null;
    Process process = null;

    try {
      process = builder.start();
      process.getOutputStream().close();

      outputCollector = new BoundedOutputCollector(process.getInputStream());
      outputReader = Thread.ofVirtual().name("command-injection-output-reader").start(outputCollector);

      boolean finished = process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      if (!finished) {
        timedOut = true;
        terminateProcessTree(process);
      } else {
        exitCode = process.exitValue();
      }

      outputReader.join(TimeUnit.SECONDS.toMillis(OUTPUT_DRAIN_TIMEOUT_SECONDS));
      if (outputReader.isAlive()) {
        timedOut = true;
        terminateProcessTree(process);
        process.getInputStream().close();
        outputReader.interrupt();
        outputReader.join(100);
      }
    } catch (IOException e) {
      executionError = e.getMessage();
      if (process != null) {
        terminateProcessTree(process);
      }
    } catch (InterruptedException e) {
      executionError = e.getMessage();
      if (process != null) {
        terminateProcessTree(process);
      }
      Thread.currentThread().interrupt();
    }

    String processOutput = outputCollector == null ? "" : outputCollector.output();
    boolean outputTruncated = outputCollector != null && outputCollector.truncated();
    if (outputTruncated) {
      processOutput += "\n[Output truncated]";
    }

    return new CommandExecutionResult(
        displayCommand, processOutput, timedOut, executionError, exitCode, outputTruncated);
  }

  private void terminateProcessTree(Process process) {
    process.toHandle().descendants().forEach(ProcessHandle::destroyForcibly);
    process.destroyForcibly();
    try {
      process.waitFor(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static final class BoundedOutputCollector implements Runnable {

    private final InputStream input;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private boolean truncated;

    private BoundedOutputCollector(InputStream input) {
      this.input = input;
    }

    @Override
    public void run() {
      byte[] buffer = new byte[4096];
      try (input) {
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
          append(buffer, bytesRead);
        }
      } catch (IOException ignored) {
        // Closing the stream is how the timeout path releases a blocked reader.
      }
    }

    private synchronized void append(byte[] buffer, int bytesRead) {
      int remaining = MAX_OUTPUT_BYTES - output.size();
      if (remaining > 0) {
        output.write(buffer, 0, Math.min(remaining, bytesRead));
      }
      if (bytesRead > remaining) {
        truncated = true;
      }
    }

    private synchronized String output() {
      return output.toString(StandardCharsets.UTF_8);
    }

    private synchronized boolean truncated() {
      return truncated;
    }
  }

  public record CommandExecutionResult(
      String command,
      String output,
      boolean timedOut,
      String executionError,
      Integer exitCode,
      boolean outputTruncated) {}
}
