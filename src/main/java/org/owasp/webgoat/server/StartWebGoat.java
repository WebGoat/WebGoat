/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 */

package org.owasp.webgoat.server;

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
    var parentBuilder =
        new SpringApplicationBuilder().parent(ParentConfig.class).web(WebApplicationType.NONE);
    parentBuilder
        .child(WebWolf.class)
        .banner(new ResourceBanner(new ClassPathResource("banner-webwolf.txt")))
        .web(WebApplicationType.SERVLET)
        .run(args);

    ApplicationContext webGoatContext =
        parentBuilder
            .child(WebGoat.class)
            .banner(new ResourceBanner(new ClassPathResource("banner-webgoat.txt")))
            .web(WebApplicationType.SERVLET)
            .run(args);

    printStartUpMessage(webGoatContext);
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
