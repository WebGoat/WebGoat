/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 */

package org.owasp.webgoat.container.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.i18n.Messages;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;


/**
 * <p>LabelService class.</p>
 *
 * @author zupzup
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class LabelService {

    public static final String URL_LABELS_MVC = "/service/labels.mvc";
    private final Messages messages;
    private final PluginMessages pluginMessages;

    /**
     * @return a map of all the labels
     */
    @GetMapping(path = URL_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Properties> fetchLabels() {
        var allProperties = new Properties();
        allProperties.putAll(messages.getMessages());
        allProperties.putAll(pluginMessages.getMessages());
        return new ResponseEntity<>(allProperties, HttpStatus.OK);
    }
}
