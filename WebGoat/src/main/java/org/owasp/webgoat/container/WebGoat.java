/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import java.io.File;
import org.owasp.webgoat.container.session.LessonSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"org.owasp.webgoat.container", "org.owasp.webgoat.lessons"})
@PropertySource("classpath:application-webgoat.properties")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"org.owasp.webgoat.container"})
@EntityScan(basePackages = "org.owasp.webgoat.container")
public class WebGoat {

  @Bean(name = "pluginTargetDirectory")
  public File pluginTargetDirectory(@Value("${webgoat.user.directory}") final String webgoatHome) {
    return new File(webgoatHome);
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public LessonSession userSessionData() {
    return new LessonSession();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
