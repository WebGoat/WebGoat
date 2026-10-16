/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyAutoConfigController {

  private final int proxyPort;

  public ProxyAutoConfigController(@Value("${ZAP_PROXY_PORT:8090}") int proxyPort) {
    this.proxyPort = proxyPort;
  }

  @GetMapping(value = "/proxy.pac", produces = "application/x-ns-proxy-autoconfig")
  public String proxyAutoConfig() {
    return "function FindProxyForURL(url, host) {\n"
        + "  return \"PROXY 127.0.0.1:" + proxyPort + "\";\n"
        + "}\n";
  }
}
