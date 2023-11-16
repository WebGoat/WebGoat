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

  @EventListener
  void onStartup(ApplicationReadyEvent event) {

    port = event.getApplicationContext().getEnvironment().getProperty("server.port");
    address = event.getApplicationContext().getEnvironment().getProperty("server.address");
    contextPath =
        event.getApplicationContext().getEnvironment().getProperty("server.servlet.context-path");
    if (StringUtils.hasText(port)
        && !StringUtils.hasText(System.getProperty("running.in.docker"))) {
      log.warn("Please browse to http://{}:{}{} to get started...", address, port, contextPath);
    }
  }

  @EventListener
  void onShutdown(ContextStoppedEvent event) {}
}
