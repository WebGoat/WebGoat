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

import org.owasp.webwolf.requests.WebWolfTraceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

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

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") String url, @Value("${spring.datasource.driver-class-name}") String driverClassName) {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url);
        driverManagerDataSource.setDriverClassName(driverClassName);
        return driverManagerDataSource;
    }
}
