package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public class Category implements Comparable {

    /** Constant <code>INTRODUCTION</code> */
    public final static Category INTRODUCTION = new Category("Introduction", new Integer(5));

    /** Constant <code>GENERAL</code> */
    public final static Category GENERAL = new Category("General", new Integer(100));

    /** Constant <code>ACCESS_CONTROL</code> */
    public final static Category ACCESS_CONTROL = new Category("Access Control Flaws", new Integer(200));

    /** Constant <code>AJAX_SECURITY</code> */
    public final static Category AJAX_SECURITY = new Category("AJAX Security", new Integer(400));

    /** Constant <code>AUTHENTICATION</code> */
    public final static Category AUTHENTICATION = new Category("Authentication Flaws", new Integer(500));

    /** Constant <code>BUFFER_OVERFLOW</code> */
    public final static Category BUFFER_OVERFLOW = new Category("Buffer Overflows", new Integer(600));

    /** Constant <code>CODE_QUALITY</code> */
    public final static Category CODE_QUALITY = new Category("Code Quality", new Integer(700));

    /** Constant <code>CONCURRENCY</code> */
    public final static Category CONCURRENCY = new Category("Concurrency", new Integer(800));

    /** Constant <code>XSS</code> */
    public final static Category XSS = new Category("Cross-Site Scripting (XSS)", new Integer(900));

    /** Constant <code>ERROR_HANDLING</code> */
    public final static Category ERROR_HANDLING = new Category("Improper Error Handling", new Integer(1000));

    /** Constant <code>INJECTION</code> */
    public final static Category INJECTION = new Category("Injection Flaws", new Integer(1100));

    /** Constant <code>DOS</code> */
    public final static Category DOS = new Category("Denial of Service", new Integer(1200));

    /** Constant <code>INSECURE_COMMUNICATION</code> */
    public final static Category INSECURE_COMMUNICATION = new Category("Insecure Communication", new Integer(1300));

    /** Constant <code>INSECURE_CONFIGURATION</code> */
    public final static Category INSECURE_CONFIGURATION = new Category("Insecure Configuration", new Integer(1400));

    /** Constant <code>INSECURE_STORAGE</code> */
    public final static Category INSECURE_STORAGE = new Category("Insecure Storage", new Integer(1500));

    /** Constant <code>MALICIOUS_EXECUTION</code> */
    public final static Category MALICIOUS_EXECUTION = new Category("Malicious Execution", new Integer(1600));

    /** Constant <code>PARAMETER_TAMPERING</code> */
    public final static Category PARAMETER_TAMPERING = new Category("Parameter Tampering", new Integer(1700));

    /** Constant <code>SESSION_MANAGEMENT</code> */
    public final static Category SESSION_MANAGEMENT = new Category("Session Management Flaws", new Integer(1800));

    /** Constant <code>WEB_SERVICES</code> */
    public final static Category WEB_SERVICES = new Category("Web Services", new Integer(1900));

    /** Constant <code>ADMIN_FUNCTIONS</code> */
    public final static Category ADMIN_FUNCTIONS = new Category("Admin Functions", new Integer(2000));

    /** Constant <code>CHALLENGE</code> */
    public final static Category CHALLENGE = new Category("Challenge", new Integer(3000));

    private static final List<Category> categories = new ArrayList<Category>();

    private String category;

    private Integer ranking;

    static {
        categories.add(INTRODUCTION);
        categories.add(PARAMETER_TAMPERING);
        categories.add(ACCESS_CONTROL);
        categories.add(AUTHENTICATION);
        categories.add(SESSION_MANAGEMENT);
        categories.add(XSS);
        categories.add(BUFFER_OVERFLOW);
        categories.add(INJECTION);
        categories.add(MALICIOUS_EXECUTION);
        categories.add(ERROR_HANDLING);
        categories.add(INSECURE_STORAGE);
        categories.add(DOS);
        categories.add(INSECURE_CONFIGURATION);
        categories.add(WEB_SERVICES);
        categories.add(AJAX_SECURITY);
        categories.add(ADMIN_FUNCTIONS);
        categories.add(GENERAL);
        categories.add(CODE_QUALITY);
        categories.add(CONCURRENCY);
        categories.add(INSECURE_COMMUNICATION);
        categories.add(CHALLENGE);
    }

    /**
     * <p>addCategory.</p>
     *
     * @param c a {@link org.owasp.webgoat.lessons.Category} object.
     */
    public static synchronized void addCategory(Category c) {
        categories.add(c);
    }

    /**
     * <p>Getter for the field <code>category</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link org.owasp.webgoat.lessons.Category} object.
     */
    public static synchronized Category getCategory(String name) {
        Iterator<Category> it = categories.iterator();
        while (it.hasNext()) {
            Category c = it.next();
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * <p>Constructor for Category.</p>
     *
     * @param category a {@link java.lang.String} object.
     * @param ranking a {@link java.lang.Integer} object.
     */
    public Category(String category, Integer ranking) {
        this.category = category;
        this.ranking = ranking;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Object obj) {
        int value = 1;

        if (obj instanceof Category) {
            value = this.getRanking().compareTo(((Category) obj).getRanking());
        }

        return value;
    }

    /**
     * <p>Getter for the field <code>ranking</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getRanking() {
        return ranking;
    }

    /**
     * <p>Setter for the field <code>ranking</code>.</p>
     *
     * @param ranking a {@link java.lang.Integer} object.
     * @return a {@link java.lang.Integer} object.
     */
    public Integer setRanking(Integer ranking) {
        return this.ranking = ranking;
    }

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return category;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Category) && getName().equals(((Category) obj).getName());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}
