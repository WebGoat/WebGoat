/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.hijacksession.cas;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoublePredicate;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 *
 * @author Angel Olle Blazquez
 *
 */

// weak id value and mechanism

@ApplicationScope
@Component
public class HijackSessionAuthenticationProvider implements AuthenticationProvider<Authentication> {

    private Queue<String> sessions = new LinkedList<>();
    private static long id = new Random().nextLong() & Long.MAX_VALUE;
    protected static final int MAX_SESSIONS = 50;

    private static final DoublePredicate PROBABILITY_DOUBLE_PREDICATE = pr -> pr < 0.75;
    private static final Supplier<String> GENERATE_SESSION_ID = () -> ++id + "-" + Instant.now().toEpochMilli();
    public static final Supplier<Authentication> AUTHENTICATION_SUPPLIER = () -> Authentication
        .builder()
        .id(GENERATE_SESSION_ID.get())
        .build();

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication == null) {
            return AUTHENTICATION_SUPPLIER.get();
        }

        if (StringUtils.isNotEmpty(authentication.getId()) && sessions.contains(authentication.getId())) {
            authentication.setAuthenticated(true);
            return authentication;
        }

        if (StringUtils.isEmpty(authentication.getId())) {
            authentication.setId(GENERATE_SESSION_ID.get());
        }

        authorizedUserAutoLogin();

        return authentication;
    }

    protected void authorizedUserAutoLogin() {
        if (!PROBABILITY_DOUBLE_PREDICATE.test(ThreadLocalRandom.current().nextDouble())) {
            Authentication authentication = AUTHENTICATION_SUPPLIER.get();
            authentication.setAuthenticated(true);
            addSession(authentication.getId());
        }
    }

    protected boolean addSession(String sessionId) {
        if (sessions.size() >= MAX_SESSIONS) {
            sessions.remove();
        }
        return sessions.add(sessionId);
    }

    protected int getSessionsSize() {
        return sessions.size();
    }

}
