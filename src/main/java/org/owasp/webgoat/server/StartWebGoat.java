/*
 * SPDX-FileCopyrightText: Copyright © 2022 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.server;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.WebGoat;
import org.owasp.webgoat.webwolf.WebWolf;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class StartWebGoat {

  public static void main(String[] args) {
    String[] applicationArgs = mapServerPortToWebGoatPort(args);
    var parentBuilder =
        new SpringApplicationBuilder().parent(ParentConfig.class).web(WebApplicationType.NONE);
    parentBuilder
        .child(WebWolf.class)
        .banner(new ResourceBanner(new ClassPathResource("banner-webwolf.txt")))
        .web(WebApplicationType.SERVLET)
        .run(applicationArgs);

    ApplicationContext webGoatContext =
        parentBuilder
            .child(WebGoat.class)
            .banner(new ResourceBanner(new ClassPathResource("banner-webgoat.txt")))
            .web(WebApplicationType.SERVLET)
            .run(applicationArgs);

    printStartUpMessage(webGoatContext);
  }

  static String[] mapServerPortToWebGoatPort(String[] args) {
    return Arrays.stream(args)
        .map(
            arg ->
                arg.startsWith("--server.port=")
                    ? "--webgoat.port=" + arg.substring("--server.port=".length())
                    : arg)
        .toArray(String[]::new);
  }

  private static void printStartUpMessage(ApplicationContext webGoatContext) {
    var url = webGoatContext.getEnvironment().getProperty("webgoat.url");
    var sslEnabled =
        webGoatContext.getEnvironment().getProperty("server.ssl.enabled", Boolean.class);
    log.warn(
        "Please browse to " + "{} to start using WebGoat...",
        sslEnabled ? url.replace("http", "https") : url);
  }
}
