/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 *
 */
package org.owasp.webgoat.service;

import org.owasp.webgoat.session.LabelDebugger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>PluginReloadService class.</p>
 *
 * @author nbaars
 * @version $Id: $Id
 */
@Controller
public class LabelDebugService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(LabelDebugService.class);

    @Autowired
    private LabelDebugger labelDebugger;

    /**
     * Reload all the plugins
     *
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @RequestMapping(value = "/debug/labels.mvc")
    public @ResponseBody
    //todo parse params to add enable / disable
    ResponseEntity<String> reloadPlugins() {
        labelDebugger.enable();
        return new ResponseEntity("Label debugger enabled refresh the WebGoat page!",HttpStatus.OK);
    }
}
