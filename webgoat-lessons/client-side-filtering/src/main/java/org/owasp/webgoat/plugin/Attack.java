package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.LessonEndpoint;
import org.owasp.webgoat.lessons.LessonEndpointMapping;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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
 * @author WebGoat
 * @version $Id: $Id
 * @since August 11, 2016
 */
@LessonEndpointMapping
public class Attack extends LessonEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody AttackResult completed(@RequestParam String answer) throws IOException {
        if ("450000".equals(answer)) {
            return trackProgress(AttackResult.success());
        } else {
            return trackProgress(AttackResult.failed("You are close, try again"));
        }
    }

    @Override
    public String getPath() {
        return "/clientSideFiltering/attack1";
    }
}
