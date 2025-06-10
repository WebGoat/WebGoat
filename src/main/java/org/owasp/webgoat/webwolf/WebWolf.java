/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import org.owasp.webgoat.webwolf.requests.WebWolfTraceRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
}
