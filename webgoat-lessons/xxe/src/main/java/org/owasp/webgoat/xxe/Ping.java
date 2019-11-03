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

package org.owasp.webgoat.xxe;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Slf4j
public class Ping {

    @Value("${webgoat.user.directory}")
    private String webGoatHomeDirectory;
    @Autowired
    private WebSession webSession;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String logRequest(@RequestHeader("User-Agent") String userAgent, @RequestParam(required = false) String text) {
        String logLine = String.format("%s %s %s", "GET", userAgent, text);
        log.debug(logLine);
        File logFile = new File(webGoatHomeDirectory, "/XXE/log" + webSession.getUserName() + ".txt");
        try {
            try (PrintWriter pw = new PrintWriter(logFile)) {
                pw.println(logLine);
            }
        } catch (FileNotFoundException e) {
            log.error("Error occurred while writing the logfile", e);
        }
        return "";
    }
}
