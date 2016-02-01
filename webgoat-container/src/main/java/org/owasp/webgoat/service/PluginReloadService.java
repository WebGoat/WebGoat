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

import org.owasp.webgoat.plugins.PluginsLoader;
import org.owasp.webgoat.session.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.nio.file.Paths;

/**
 * <p>PluginReloadService class.</p>
 *
 * @author nbaars
 * @version $Id: $Id
 */
@Controller
public class PluginReloadService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PluginReloadService.class);

    /**
     * Reload all the plugins
     *
     * @param session a {@link javax.servlet.http.HttpSession} object.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @RequestMapping(value = "/reloadplugins.mvc")
    public @ResponseBody
    ResponseEntity<String> reloadPlugins(HttpSession session) {
        WebSession webSession = (WebSession) session.getAttribute(WebSession.SESSION);
        logger.debug("Loading plugins into cache");
        String pluginPath = session.getServletContext().getRealPath("plugin_lessons");
        String targetPath = session.getServletContext().getRealPath("plugin_extracted");
        new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)).copyJars();

        webSession.getCourse().loadLessonFromPlugin(session.getServletContext());
        return new ResponseEntity("Plugins reload refresh the WebGoat page!",HttpStatus.OK);
    }
}
