package org.owasp.webgoat.plugins;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.Endpoint;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.List;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 *
 * @author nbaars
 * @version $Id: $Id
 * @since October 16, 2016
 */
@Slf4j
public class PluginEndpointPublisher {

    private AbstractApplicationContext applicationContext;

    public PluginEndpointPublisher(ApplicationContext applicationContext) {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }

    public void publish(List<Class<Endpoint>> endpoints) {
        endpoints.forEach(e -> publishEndpoint(e));
    }

    private void publishEndpoint(Class<? extends MvcEndpoint> e) {
        try {
            BeanDefinition beanDefinition = new RootBeanDefinition(e, Autowire.BY_TYPE.value(), true);
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
            beanFactory.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
        } catch (Exception ex) {
            log.error("Failed to register " + e.getSimpleName() + " as endpoint with Spring, skipping...");
        }
    }
}
