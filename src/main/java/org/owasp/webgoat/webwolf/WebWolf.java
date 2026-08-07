/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.owasp.webgoat.webwolf.requests.WebWolfTraceRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("org.owasp.webgoat.webwolf")
@PropertySource("classpath:application-webwolf.properties")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"org.owasp.webgoat.webwolf"})
@EntityScan(basePackages = "org.owasp.webgoat.webwolf")
public class WebWolf {

  @Bean
  public HttpExchangeRepository traceRepository() {
    return new WebWolfTraceRepository();
  }

  /**
   * Spring Boot 4 auto-configures a Jackson 3 mapper for the HTTP layer. WebWolf runs in its own
   * application context, so it needs its own Jackson 2 {@link ObjectMapper} for the components that
   * inject one directly (e.g. {@code Requests}).
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
