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

package org.owasp.webgoat.webwolf.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTrace.Request;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;

import static java.util.stream.Collectors.toList;

/**
 * Controller for fetching all the HTTP requests from WebGoat to WebWolf for a specific
 * user.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/requests")
public class Requests {

    private final WebWolfTraceRepository traceRepository;
    private final ObjectMapper objectMapper;

    @AllArgsConstructor
    @Getter
    private class Tracert {
        private final Instant date;
        private final String path;
        private final String json;
    }

    @GetMapping
    public ModelAndView get() {
        var model = new ModelAndView("requests");
        var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var traces = traceRepository.findAllTraces().stream()
        		.filter(t -> allowedTrace(t, user))
                .map(t -> new Tracert(t.getTimestamp(), path(t), toJsonString(t))).collect(toList());
        model.addObject("traces", traces);

        return model;
    }
    
    private boolean allowedTrace(HttpTrace t, UserDetails user) {
    	Request req = t.getRequest();
    	boolean allowed = true;
    	/* do not show certain traces to other users in a classroom setup */
    	if (req.getUri().getPath().contains("/files") && !req.getUri().getPath().contains(user.getUsername())) {
    		allowed = false;
    	} else if (req.getUri().getPath().contains("/landing") && req.getUri().getQuery()!=null && req.getUri().getQuery().contains("uniqueCode") && !req.getUri().getQuery().contains(StringUtils.reverse(user.getUsername()))) {
    		allowed = false;
    	}
    	    	
    	return allowed;
    }

    private String path(HttpTrace t) {
        return (String) t.getRequest().getUri().getPath();
    }

    private String toJsonString(HttpTrace t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("Unable to create json", e);
        }
        return "No request(s) found";
    }
}
