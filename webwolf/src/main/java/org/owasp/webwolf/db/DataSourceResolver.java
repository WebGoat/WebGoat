/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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

package org.owasp.webwolf.db;

import static org.owasp.webwolf.db.ActuatorDsJsonParser.getDsHealthStatus;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Angel Olle Blazquez
 *
 */

@Slf4j
@Component
@EnableRetry
public class DataSourceResolver {

    @Value("${webgoat.actuator.base.url}")
    private String baseUrl;

    @Value("${webgoat.actuator.health.db.path:/health}")
    private String dbHealthPath;

    @Value("${webgoat.actuator.configprops.path:/configprops}")
    private String configPropsPath;
    
    @Autowired
    ApplicationContext ctx;

    @Bean
    @DependsOn("dsConfigDiscovery")
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(dataSourceProperties.getUrl());
        driverManagerDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        return driverManagerDataSource;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Retryable(maxAttemptsExpression = "${webwolf.datasource-discovery.retry.max-attempts:5}",
        value = Exception.class,
        backoff = @Backoff(
            multiplierExpression = "${webwolf.datasource-discovery.retry.backoff.multiplier:1.5}",
            maxDelayExpression = "${webwolf.datasource-discovery.retry.backoff.max-delay:30000}",
            delayExpression = "${webwolf.datasource-discovery.retry.backoff.delay:5000}"))
    public DataSourceProperties dsConfigDiscovery(RestTemplate restTemplate) {
        healthCheck(restTemplate);
        return restTemplate.getForObject(baseUrl + configPropsPath, DataSourceProperties.class);
    }

    public void healthCheck(RestTemplate restTemplate) {
        log.info("Checking database availability (make sure WebGoat is running)...");
        JsonNode json = restTemplate.getForObject(baseUrl + dbHealthPath, JsonNode.class);
        String status = getDsHealthStatus(json);
        if (!status.equals("UP")) {
            throw new ResourceUnavailableException();
        }
    }

    @Recover
    public DataSourceProperties exitOnResourceUnavailable(Exception e, RestTemplate restTemplate) {
        log.error("It seems that the required database is not running. Please start WebGoat with the integrated or standalone database first.");
        System.exit(SpringApplication.exit(ctx, () -> 1));
        return null;
    }
}
