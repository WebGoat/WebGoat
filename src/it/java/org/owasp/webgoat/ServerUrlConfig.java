/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat;

public record ServerUrlConfig(String host, String port, String contextPath) {

  public ServerUrlConfig {
    contextPath = contextPath.replaceAll("/", "");
  }

  public String getBaseUrl() {
    return "http://%s:%s".formatted(host, port);
  }

  public String url(String path) {
    return "%s/%s".formatted(getFullUrl(), path);
  }

  private String getFullUrl() {
    return "http://%s:%s/%s".formatted(host, port, contextPath);
  }

  public static ServerUrlConfig webGoat() {
    return new ServerUrlConfig(
        "localhost", env("WEBGOAT_PORT", "8080"), env("WEBGOAT_CONTEXT", "WebGoat"));
  }

  public static ServerUrlConfig webWolf() {
    return new ServerUrlConfig(
        "localhost", env("WEBWOLF_PORT", "9090"), env("WEBWOLF_CONTEXT", "WebWolf"));
  }

  private static String env(String variableName, String defaultValue) {
    return System.getenv().getOrDefault(variableName, "").isEmpty()
        ? defaultValue
        : System.getenv(variableName);
  }
}
