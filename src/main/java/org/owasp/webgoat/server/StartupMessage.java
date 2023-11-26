package org.owasp.webgoat.server;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@NoArgsConstructor
public class StartupMessage {

  private String port;
  private String address;
  private String contextPath;

  private String applicationName;

  private static boolean useSSL =
      Boolean.valueOf(System.getenv().getOrDefault("WEBGOAT_SSLENABLED", "true"));

  @EventListener
  void onStartup(ApplicationReadyEvent event) {

    port = event.getApplicationContext().getEnvironment().getProperty("server.port");
    address = event.getApplicationContext().getEnvironment().getProperty("server.address");
    contextPath =
        event.getApplicationContext().getEnvironment().getProperty("server.servlet.context-path");
    applicationName =
        event.getApplicationContext().getEnvironment().getProperty("spring.application.name");
    if (StringUtils.hasText(applicationName)) {
      if (applicationName.equals("WebGoat")) {
        log.warn(
            "Please browse to "
                + (useSSL ? "https://" : "http://")
                + "{}:{}{} to start using WebGoat...",
            event.getApplicationContext().getEnvironment().getProperty("webgoat.host"),
            port,
            contextPath);
      } else {
        log.warn(
            "Please browse to http://{}:{}{} to start using WebWolf...",
            event.getApplicationContext().getEnvironment().getProperty("webwolf.host"),
            port,
            contextPath);
      }
    }
  }

  @EventListener
  void onShutdown(ContextStoppedEvent event) {}
}
