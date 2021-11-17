/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webwolf;

import java.io.IOException;
import java.net.Socket;

import org.owasp.webwolf.requests.WebWolfTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class WebWolf {

    @Bean
    public HttpTraceRepository traceRepository() {
        return new WebWolfTraceRepository();
    }

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-webwolf");

        String webwolfPort = System.getenv("WEBWOLF_PORT");
        String webWolfHost = null == System.getenv("WEBWOLF_HOST") ? "127.0.0.1" : System.getenv("WEBWOLF_HOST");
        String fileEncoding = System.getProperty("file.encoding");

        int wolfPort = webwolfPort == null ? 9090 : Integer.parseInt(webwolfPort);

        if (null == fileEncoding || !fileEncoding.equals("UTF-8")) {
            System.out.println("It seems the application is started on a OS with non default UTF-8 encoding:" + fileEncoding);
            System.out.println("Please add: -Dfile.encoding=UTF-8");
            System.exit(-1);
        }

        if (isAlreadyRunning(webWolfHost, wolfPort)) {
            System.out.println("Port " + webWolfHost + ":" + wolfPort + " is in use. Use environment value WEBWOLF_PORT to set a different value.");
            System.exit(-1);
        }
        SpringApplication.run(WebWolf.class, args);
    }

    private static boolean isAlreadyRunning(String host, int port) {
        try (var ignored = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
