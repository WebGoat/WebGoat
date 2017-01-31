/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
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
 */

package org.owasp.webgoat.assignments;

import org.mockito.Mock;
import org.owasp.webgoat.i18n.Messages;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class AssignmentEndpointTest {

    @Mock
    protected UserTracker userTracker;
    @Mock
    protected WebSession webSession;
    @Mock
    protected UserSessionData userSessionData;
    protected Messages messages = new Messages(new LocaleResolver() {
        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            return Locale.ENGLISH;
        }

        @Override
        public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

        }}){
        @Override
        protected Locale resolveLocale() {
            return Locale.ENGLISH;
        }
    };

    public void init(AssignmentEndpoint a) {
        messages.setBasenames("classpath:/i18n/messages", "classpath:/plugin/i18n/WebGoatLabels");
        ReflectionTestUtils.setField(a, "userTracker", userTracker);
        ReflectionTestUtils.setField(a, "userSessionData", userSessionData);
        ReflectionTestUtils.setField(a, "webSession", webSession);
        ReflectionTestUtils.setField(a, "messages", messages);
    }

}