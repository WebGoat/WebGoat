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

package org.owasp.webgoat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.StringUtils;

/**
 * Main entry point, this project is here to get all the lesson jars included to the final jar file
 *
 * @author nbaars
 * @date 2/21/17
 */
@SpringBootApplication(scanBasePackages = "org.owasp.webgoat")
@ServletComponentScan
@Slf4j
public class StartWebGoat extends SpringBootServletInitializer {

    public static void main(String[] args) {
        log.info("Starting WebGoat with args: {}", StringUtils.arrayToCommaDelimitedString(args));
        System.setProperty("spring.config.name", "application-webgoat");
       
        String webgoatPort  = System.getenv("WEBGOAT_PORT");
        String databasePort = System.getenv("WEBGOAT_HSQLPORT"); 
        String webGoatHost = null==System.getenv("WEBGOAT_HOST")?"127.0.0.1":System.getenv("WEBGOAT_HOST");
        int goatPort = webgoatPort == null?8080:Integer.parseInt(webgoatPort);
        int dbPort = databasePort == null?9001:Integer.parseInt(databasePort);
        
        if (isAlreadyRunning(webGoatHost, goatPort)) {
        	log.error("Port {}:{} is already in use", webGoatHost, goatPort);
        	System.out.println("Port "+webGoatHost+":"+goatPort+" is in use. Use environment value WEBGOAT_PORT to set a different value.");
        	System.exit(-1);
        }
        if (isAlreadyRunning(webGoatHost, dbPort)) {
        	log.error("Port {}:{} is already in use", webGoatHost, goatPort);
        	System.out.println("Port "+webGoatHost+":"+goatPort+" is in use. Use environment value WEBGOAT_HSQLPORT to set a different value.");
        	System.exit(-1);
        }
        SpringApplication.run(StartWebGoat.class, args);
    }
    
    private static boolean isAlreadyRunning(String host, int port) {
        try (var ignored = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
