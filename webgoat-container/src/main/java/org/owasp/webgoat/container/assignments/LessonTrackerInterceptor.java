/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
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
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.container.assignments;

import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class LessonTrackerInterceptor implements ResponseBodyAdvice<Object> {

    private UserTrackerRepository userTrackerRepository;
    private WebSession webSession;

    public LessonTrackerInterceptor(UserTrackerRepository userTrackerRepository, WebSession webSession) {
        this.userTrackerRepository = userTrackerRepository;
        this.webSession = webSession;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof AttackResult attackResult) {
            trackProgress(attackResult);
        }
        return o;
    }


    protected AttackResult trackProgress(AttackResult attackResult) {
        UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
        if (userTracker == null) {
            userTracker = new UserTracker(webSession.getUserName());
        }
        if (attackResult.assignmentSolved()) {
            userTracker.assignmentSolved(webSession.getCurrentLesson(), attackResult.getAssignment());
        } else {
            userTracker.assignmentFailed(webSession.getCurrentLesson());
        }
        userTrackerRepository.saveAndFlush(userTracker);
        return attackResult;
    }
}
