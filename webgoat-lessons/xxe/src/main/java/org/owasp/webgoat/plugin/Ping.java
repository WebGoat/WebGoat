package org.owasp.webgoat.plugin;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.Endpoint;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
 * @since November 17, 2016
 */
@Slf4j
public class Ping extends Endpoint {

    @Override
    public String getPath() {
        return "XXE/ping";
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String logRequest(@RequestHeader("User-Agent") String userAgent, @RequestParam(required = false) String text) {
        String logLine = String.format("%s %s %s", "GET", userAgent, text);
        log.debug(logLine);
        File logFile = new File(getPluginDirectory(), "/XXE/log.txt");
        try {
            try (PrintWriter pw = new PrintWriter(logFile)) {
                pw.println(logLine);
            }
        } catch (FileNotFoundException e) {
            log.error("Error occured while writing the logfile", e);
        }
        return "";
    }
}
