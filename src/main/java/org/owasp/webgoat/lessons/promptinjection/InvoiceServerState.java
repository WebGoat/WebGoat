/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.stereotype.Component;

/**
 * In-memory, per-learner state of the lesson's fake invoice MCP server. The learner's AI assistant
 * has no WebGoat session cookie, so the MCP endpoint identifies the learner through a random token
 * in its URL. A second random value, the {@code session_token} returned by {@code get_account}, is
 * the canary: it should never appear in another tool's arguments.
 */
@Component
public class InvoiceServerState implements Initializable {

  static final int MAX_CALLS = 50;

  private static final SecureRandom RANDOM = new SecureRandom();

  private final Map<String, Learner> learners = new ConcurrentHashMap<>();
  private final Map<String, String> usernamesByMcpToken = new ConcurrentHashMap<>();

  /** One logged {@code tools/call}. */
  public record ToolCall(
      String time,
      String tool,
      Map<String, String> arguments,
      boolean leaked,
      boolean refused,
      String variant,
      boolean stripped) {}

  /** Server settings the learner controls from the lesson page. */
  public record Settings(DescriptionVariant variant, boolean argumentGuard, boolean pinned) {}

  public static final class Learner {
    private final String username;
    private final String mcpToken;
    private String sessionToken;
    private String accountRef;
    private Settings settings;
    private final Deque<ToolCall> calls = new ArrayDeque<>();

    private Learner(String username, String mcpToken) {
      this.username = username;
      this.mcpToken = mcpToken;
      reset();
    }

    private synchronized void reset() {
      this.sessionToken = "WG-SECRET-" + random(6);
      this.accountRef = "WG-ACC-" + random(3).toUpperCase(Locale.ROOT);
      this.settings = new Settings(DescriptionVariant.TASK_SHAPED, false, false);
      this.calls.clear();
    }

    public String username() {
      return username;
    }

    public String mcpToken() {
      return mcpToken;
    }

    public synchronized String sessionToken() {
      return sessionToken;
    }

    public synchronized String accountRef() {
      return accountRef;
    }

    public synchronized Settings settings() {
      return settings;
    }

    synchronized void settings(Settings settings) {
      this.settings = settings;
    }

    synchronized void record(ToolCall call) {
      calls.addLast(call);
      while (calls.size() > MAX_CALLS) {
        calls.removeFirst();
      }
    }

    public synchronized List<ToolCall> calls() {
      return List.copyOf(calls);
    }

    /** A token-bearing call the server accepted: the injection worked. */
    public synchronized Optional<ToolCall> acceptedLeak() {
      return calls.stream().filter(c -> c.leaked() && !c.refused()).reduce((a, b) -> b);
    }

    /** A token-bearing call the argument guard refused: the defence held. */
    public synchronized Optional<ToolCall> refusedLeak() {
      return calls.stream().filter(c -> c.leaked() && c.refused()).reduce((a, b) -> b);
    }
  }

  public Learner forUser(String username) {
    return learners.computeIfAbsent(
        username,
        name -> {
          String mcpToken = random(16);
          usernamesByMcpToken.put(mcpToken, name);
          return new Learner(name, mcpToken);
        });
  }

  public Optional<Learner> forMcpToken(String mcpToken) {
    return Optional.ofNullable(usernamesByMcpToken.get(mcpToken)).map(learners::get);
  }

  /**
   * Called on lesson reset. Rotates the canary, restores the vulnerable defaults and clears the
   * call log. The MCP URL stays the same so the learner does not need to reconnect the assistant.
   */
  @Override
  public void initialize(WebGoatUser user) {
    Learner learner = learners.get(user.getUsername());
    if (learner != null) {
      learner.reset();
    }
  }

  private static String random(int bytes) {
    byte[] value = new byte[bytes];
    RANDOM.nextBytes(value);
    return HexFormat.of().formatHex(value);
  }
}
