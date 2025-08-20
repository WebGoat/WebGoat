/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf;

import jakarta.annotation.PostConstruct;
import java.io.File;
import org.owasp.webgoat.container.UserInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

  @Value("${webwolf.fileserver.location}")
  private String fileLocation;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/files/**").addResourceLocations("file:///" + fileLocation + "/");

    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/webwolf/static/css/");
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/webwolf/static/js/");
    registry
        .addResourceHandler("/images/**")
        .addResourceLocations("classpath:/webwolf/static/images/");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login").setViewName("webwolf-login");
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/").setViewName("home");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new UserInterceptor());
  }

  @PostConstruct
  public void createDirectory() {
    File file = new File(fileLocation);
    if (!file.exists()) {
      file.mkdirs();
    }
  }
}
