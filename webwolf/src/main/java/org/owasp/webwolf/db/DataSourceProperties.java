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

import static org.owasp.webwolf.db.ActuatorDsJsonParser.getDsPropertyFromConfigProps;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * 
 * @author Angel Olle Blazquez
 *
 */

@Data
public class DataSourceProperties implements Serializable {
    private static final long serialVersionUID = -5897408528235134090L;
    private String url;
    private String driverClassName;

    @JsonProperty("contexts")
    protected void props(JsonNode node) {
        url = getDsPropertyFromConfigProps(node, "url");
        driverClassName = getDsPropertyFromConfigProps(node, "driverClassName");
    }

}
