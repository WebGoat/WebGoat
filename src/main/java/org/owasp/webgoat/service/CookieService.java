/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 *
 * For details, please see http://code.google.com/p/webgoat/
 */
package org.owasp.webgoat.service;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class CookieService extends BaseService {

    /**
     * Returns cookies for last attack
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/cookie.mvc", produces = "application/json")
    public @ResponseBody
    List<Cookie> showCookies(HttpSession session) {
        WebSession ws = getWebSession(session);
        List<Cookie> cookies = ws.getCookiesOnLastRequest();
        return cookies;
    }
}
