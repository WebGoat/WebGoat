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

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Angel Olle Blazquez
 *
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActuatorDsJsonParser {

    protected static final String getDsPropertyFromConfigProps(JsonNode node, String propertyName) {
        return node
            .get("application")
            .get("beans")
            .get("spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties")
            .get("properties")
            .get(propertyName)
            .asText();
    }

    protected static final String getDsHealthStatus(JsonNode node) {
        return node
            .get("components")
            .get("db")
            .get("components")
            .get("dataSource")
            .get("status")
            .asText();
    }
}
