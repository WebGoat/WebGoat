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
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.SocketUtils;

import java.lang.reflect.Method;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Slf4j
public class StartWebGoat {

    public static final String WEBGOAT_PORT = "webgoat.port";
    public static final String WEBWOLF_PORT = "webwolf.port";

    private static final int MAX_PORT = 9999;

    public static void main(String[] args) {
        setEnvironmentVariableForPort(WEBGOAT_PORT, "8080");
        setEnvironmentVariableForPort(WEBWOLF_PORT, "9090");

        new SpringApplicationBuilder().parent(ParentConfig.class)
                .web(WebApplicationType.NONE).bannerMode(Banner.Mode.OFF)
                .child(WebGoat.class)
                .web(WebApplicationType.SERVLET)
                .sibling(WebWolf.class).bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    private static void setEnvironmentVariableForPort(String name, String defaultValue) {
        ofNullable(System.getProperty(name))
                .or(() -> of(defaultValue))
                .map(Integer::parseInt)
                .map(port -> findPort(port))
                .ifPresent(port -> System.setProperty(name, port));
    }

    public static String findPort(int port) {
        try {
            if (port == MAX_PORT) {
                log.error("No free port found from 8080 - {}", MAX_PORT);
                return "" + port;
            }
            return "" + SocketUtils.findAvailableTcpPort(port, port);
        } catch (IllegalStateException var4) {
            return findPort(port + 1);
        }
    }

}
