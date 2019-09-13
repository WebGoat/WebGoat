package org.owasp.webwolf;

import org.owasp.webwolf.requests.WebWolfTraceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebWolf {

    @Bean
    public HttpTraceRepository traceRepository() {
        return new WebWolfTraceRepository();
    }

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-webwolf");
        SpringApplication.run(WebWolf.class, args);
    }
}
