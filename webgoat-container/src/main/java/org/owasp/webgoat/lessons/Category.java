package org.owasp.webgoat.lessons;

import lombok.Getter;

/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 28, 2003
 */
public enum Category {

    INTRODUCTION("Introduction", new Integer(5)),
    GENERAL("General", new Integer(100)),
    INJECTION("Injection Flaws", new Integer(200)),
    AUTHENTICATION("Authentication Flaws", new Integer(300)),
    XSS("Cross-Site Scripting (XSS)", new Integer(400)),
    REQ_FORGERIES("Request Forgeries", new Integer(450)),
    ACCESS_CONTROL("Access Control Flaws", new Integer(500)),
    INSECURE_CONFIGURATION("Insecure Configuration", new Integer(600)),
    INSECURE_COMMUNICATION("Insecure Communication", new Integer(700)),
    INSECURE_STORAGE("Insecure Storage", new Integer(800)),
    INSECURE_DESERIALIZATION("Insecure Deserialization", new Integer(850)),
    REQUEST_FORGERIES("Request Forgeries", new Integer(900)),
    VULNERABLE_COMPONENTS("Vulnerable Components - A9", new Integer(950)),
    AJAX_SECURITY("AJAX Security", new Integer(1000)),
    BUFFER_OVERFLOW("Buffer Overflows", new Integer(1100)),
    CODE_QUALITY("Code Quality", new Integer(1200)),
    CONCURRENCY("Concurrency", new Integer(1300)),
    ERROR_HANDLING("Improper Error Handling", new Integer(1400)),
    DOS("Denial of Service", new Integer(1500)),
    MALICIOUS_EXECUTION("Malicious Execution", new Integer(1600)),
    CLIENT_SIDE("Client side", new Integer(1700)),
    SESSION_MANAGEMENT("Session Management Flaws", new Integer(1800)),
    WEB_SERVICES("Web Services", new Integer(1900)),
    ADMIN_FUNCTIONS("Admin Functions", new Integer(2000)),
    CHALLENGE("Challenges", new Integer(3000));

    @Getter
    private String name;
    @Getter
    private Integer ranking;

    Category(String name, Integer ranking) {
        this.name = name;
        this.ranking = ranking;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }
}
