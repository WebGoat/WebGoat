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

    INTRODUCTION("Introduction", 5),
    GENERAL("General", 100),
    
    INJECTION("(A1) Injection", 300),
    AUTHENTICATION("(A2) Broken Authentication", 302),
    INSECURE_COMMUNICATION("(A3) Sensitive Data Exposure", 303),
    XXE("(A4) XML External Entities (XXE)", 304),
    ACCESS_CONTROL("(A5) Broken Access Control", 305),
    
    XSS("(A7) Cross-Site Scripting (XSS)", 307),
    INSECURE_DESERIALIZATION("(A8) Insecure Deserialization", 308),
    VULNERABLE_COMPONENTS("(A9) Vulnerable Components", 309),
    
    REQUEST_FORGERIES("(A8:2013) Request Forgeries", 318),

    
    REQ_FORGERIES("Request Forgeries", 450),
    
    INSECURE_CONFIGURATION("Insecure Configuration", 600),
    INSECURE_STORAGE("Insecure Storage", 800),
    
    
    AJAX_SECURITY("AJAX Security", 1000),
    BUFFER_OVERFLOW("Buffer Overflows", 1100),
    CODE_QUALITY("Code Quality", 1200),
    CONCURRENCY("Concurrency", 1300),
    ERROR_HANDLING("Improper Error Handling", 1400),
    DOS("Denial of Service", 1500),
    MALICIOUS_EXECUTION("Malicious Execution", 1600),
    CLIENT_SIDE("Client side", 1700),
    SESSION_MANAGEMENT("Session Management Flaws", 1800),
    WEB_SERVICES("Web Services", 1900),
    ADMIN_FUNCTIONS("Admin Functions", 2000),
    CHALLENGE("Challenges", 3000);

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
