/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class SessionService extends BaseService {

    /**
     * Returns hints for current lesson
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/session.mvc", produces = "application/json")
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
