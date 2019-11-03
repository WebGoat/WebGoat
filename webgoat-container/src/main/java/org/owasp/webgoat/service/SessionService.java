/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.owasp.webgoat.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>SessionService class.</p>
 *
 * @author rlawson
 * @version $Id: $Id
 */
@Controller
public class SessionService {

    /**
     * Returns hints for current lesson
     *
     * @param session a {@link javax.servlet.http.HttpSession} object.
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link java.lang.String} object.
     */
    @RequestMapping(path = "/service/session.mvc", produces = "application/json")
    public @ResponseBody
    String showSession(HttpServletRequest request, HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("id").append(" = ").append(session.getId()).append("\n");
        sb.append("created").append(" = ").append(new Date(session.getCreationTime())).append("\n");
        sb.append("last access").append(" = ").append(new Date(session.getLastAccessedTime())).append("\n");
        sb.append("timeout (secs)").append(" = ").append(session.getMaxInactiveInterval()).append("\n");
        sb.append("session from cookie?").append(" = ").append(request.isRequestedSessionIdFromCookie()).append("\n");
        sb.append("session from url?").append(" = ").append(request.isRequestedSessionIdFromURL()).append("\n");
        sb.append("=====================================\n");
        // get attributes
        List<String> attributes = new ArrayList<String>();
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            attributes.add(name);
        }
        Collections.sort(attributes);
        for (String attribute : attributes) {
            String value = session.getAttribute(attribute) + "";
            sb.append(attribute).append(" = ").append(value).append("\n");
        }
        return sb.toString();
    }
}
