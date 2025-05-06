/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.webwolf.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for fetching all the HTTP requests from WebGoat to WebWolf for a specific user.
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
  public ModelAndView get(Authentication authentication) {
    var model = new ModelAndView("requests");
    String username = (null != authentication) ? authentication.getName() : "anonymous";
    var traces =
        traceRepository.findAll().stream()
            .filter(t -> allowedTrace(t, username))
            .map(t -> new Tracert(t.getTimestamp(), path(t), toJsonString(t)))
            .toList();
    model.addObject("traces", traces);

    return model;
  }

  private boolean allowedTrace(HttpExchange t, String username) {
    HttpExchange.Request req = t.getRequest();
    boolean allowed = true;
    /* do not show certain traces to other users in a classroom setup */
    if (req.getUri().getPath().contains("/files") && !req.getUri().getPath().contains(username)) {
      allowed = false;
    } else if (req.getUri().getPath().contains("/landing")
        && req.getUri().getQuery() != null
        && req.getUri().getQuery().contains("uniqueCode")
        && !req.getUri().getQuery().contains(StringUtils.reverse(username))) {
      allowed = false;
    }

    return allowed;
  }

  private String path(HttpExchange t) {
    return t.getRequest().getUri().getPath();
  }

  private String toJsonString(HttpExchange t) {
    try {
      return objectMapper.writeValueAsString(t);
    } catch (JsonProcessingException e) {
      log.error("Unable to create json", e);
    }
    return "No request(s) found";
  }
}
