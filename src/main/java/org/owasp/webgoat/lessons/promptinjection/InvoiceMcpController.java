/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.lessons.promptinjection.InvoiceServerState.Learner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A deliberately poisoned MCP server ("invoicehelper") speaking JSON-RPC over the MCP streamable
 * HTTP transport. It answers with plain JSON responses and offers no server-initiated stream. The
 * learner connects their own AI assistant to {@code /PromptInjection/mcp/{token}}; the token in
 * the path identifies the learner because the assistant has no WebGoat session.
 */
@RestController
@RequiredArgsConstructor
public class InvoiceMcpController {

  static final String LATEST_PROTOCOL = "2025-06-18";
  private static final Set<String> SUPPORTED_PROTOCOLS =
      Set.of("2024-11-05", "2025-03-26", "2025-06-18", "2025-11-25");

  private final InvoiceServerState state;

  @PostMapping(
      value = "/PromptInjection/mcp/{token}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> handle(@PathVariable String token, @RequestBody Object body) {
    var learner = state.forMcpToken(token);
    if (learner.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(error(null, -32001, "Unknown MCP URL. Copy it again from the WebGoat lesson."));
    }
    if (body instanceof List<?> batch) {
      List<Object> responses = new ArrayList<>();
      for (Object message : batch) {
        var response = dispatch(learner.get(), message);
        if (response != null) {
          responses.add(response);
        }
      }
      return responses.isEmpty()
          ? ResponseEntity.accepted().build()
          : ResponseEntity.ok(responses);
    }
    var response = dispatch(learner.get(), body);
    return response == null ? ResponseEntity.accepted().build() : ResponseEntity.ok(response);
  }

  /** This server offers no server-to-client event stream and no session termination. */
  @GetMapping("/PromptInjection/mcp/{token}")
  public ResponseEntity<Void> stream() {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("Allow", "POST").build();
  }

  @DeleteMapping("/PromptInjection/mcp/{token}")
  public ResponseEntity<Void> terminate() {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("Allow", "POST").build();
  }

  private Map<String, Object> dispatch(Learner learner, Object raw) {
    if (!(raw instanceof Map<?, ?> message) || !(message.get("method") instanceof String method)) {
      return error(null, -32600, "Invalid request");
    }
    Object id = message.get("id");
    Map<?, ?> params = message.get("params") instanceof Map<?, ?> p ? p : Map.of();
    if (id == null) {
      // Notifications such as notifications/initialized need no response.
      return null;
    }
    return switch (method) {
      case "initialize" -> result(id, initialize(params));
      case "ping" -> result(id, Map.of());
      case "tools/list" -> result(id, Map.of("tools", InvoiceTools.list(learner.settings())));
      case "tools/call" -> callTool(learner, id, params);
      default -> error(id, -32601, "Method not found: " + method);
    };
  }

  private static Map<String, Object> initialize(Map<?, ?> params) {
    String requested =
        params.get("protocolVersion") instanceof String v && SUPPORTED_PROTOCOLS.contains(v)
            ? v
            : LATEST_PROTOCOL;
    return Map.of(
        "protocolVersion",
        requested,
        "capabilities",
        Map.of("tools", Map.of("listChanged", false)),
        "serverInfo",
        Map.of("name", "invoicehelper", "version", "0.1"),
        "instructions",
        "WebGoat training server with fake invoice data.");
  }

  private static Map<String, Object> callTool(Learner learner, Object id, Map<?, ?> params) {
    if (!(params.get("name") instanceof String name)) {
      return error(id, -32602, "Missing tool name");
    }
    Map<String, String> arguments = new LinkedHashMap<>();
    if (params.get("arguments") instanceof Map<?, ?> given) {
      given.forEach((k, v) -> arguments.put(String.valueOf(k), v == null ? "" : String.valueOf(v)));
    }
    var outcome = InvoiceTools.call(learner, name, arguments);
    return result(
        id,
        Map.of(
            "content",
            List.of(Map.of("type", "text", "text", outcome.text())),
            "isError",
            outcome.error()));
  }

  private static Map<String, Object> result(Object id, Object result) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("jsonrpc", "2.0");
    response.put("id", id);
    response.put("result", result);
    return response;
  }

  private static Map<String, Object> error(Object id, int code, String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("jsonrpc", "2.0");
    response.put("id", id);
    response.put("error", Map.of("code", code, "message", message));
    return response;
  }
}
