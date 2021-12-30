package org.owasp.webgoat.server;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class StartupMessage {

    private String port;
    private String address;

    @EventListener
    void onStartup(ApplicationReadyEvent event) {
        if (event.getApplicationContext().getApplicationName().contains("WebGoat")) {
            port = event.getApplicationContext().getEnvironment().getProperty("server.port");
            address = event.getApplicationContext().getEnvironment().getProperty("server.address");
        }
        if (event.getApplicationContext().getApplicationName().contains("WebWolf")) {
            log.info("Please browse to http://{}:{}/WebGoat to get started...", address, port);
        }
    }

    @EventListener
    void onShutdown(ContextStoppedEvent event) {
    }
}
