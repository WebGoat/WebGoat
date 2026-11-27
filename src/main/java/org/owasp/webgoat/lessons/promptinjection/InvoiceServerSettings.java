/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.lessons.promptinjection.InvoiceServerState.Learner;
import org.owasp.webgoat.lessons.promptinjection.InvoiceServerState.Settings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Authenticated lesson-page controls for the learner's invoice MCP server: the per-user MCP URL,
 * the description variant, the hardening switches and the server's call log.
 */
@RestController
@RequiredArgsConstructor
public class InvoiceServerSettings {

  private final InvoiceServerState state;

  @GetMapping("/PromptInjection/server")
  public ResponseEntity<Map<String, Object>> view(@CurrentUsername String username) {
    return noStore(view(state.forUser(username)));
  }

  @PostMapping("/PromptInjection/server")
  public ResponseEntity<Map<String, Object>> update(
      @CurrentUsername String username,
      @RequestParam(required = false) String variant,
      @RequestParam(required = false) Boolean argumentGuard,
      @RequestParam(required = false) Boolean pinned) {
    Learner learner = state.forUser(username);
    Settings current = learner.settings();
    learner.settings(
        new Settings(
            variant == null ? current.variant() : DescriptionVariant.fromId(variant),
            argumentGuard == null ? current.argumentGuard() : argumentGuard,
            pinned == null ? current.pinned() : pinned));
    return noStore(view(learner));
  }

  private static Map<String, Object> view(Learner learner) {
    String mcpUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/PromptInjection/mcp/{token}")
            .buildAndExpand(learner.mcpToken())
            .toUriString();
    Settings settings = learner.settings();
    Map<String, Object> view = new LinkedHashMap<>();
    view.put("mcpUrl", mcpUrl);
    view.put("notesUrl", mcpUrl + "/release-notes");
    view.put("variant", settings.variant().id());
    view.put("argumentGuard", settings.argumentGuard());
    view.put("pinned", settings.pinned());
    view.put("tools", InvoiceTools.list(settings));
    view.put("calls", learner.calls());
    return view;
  }

  private static ResponseEntity<Map<String, Object>> noStore(Map<String, Object> body) {
    return ResponseEntity.ok().header("Cache-Control", "no-store").body(body);
  }
}
