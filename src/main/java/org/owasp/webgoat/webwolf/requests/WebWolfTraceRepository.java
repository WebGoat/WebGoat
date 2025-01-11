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

import static org.owasp.webgoat.webwolf.requests.WebWolfTraceRepository.Exclusion.contains;
import static org.owasp.webgoat.webwolf.requests.WebWolfTraceRepository.Exclusion.endsWith;

import com.google.common.collect.EvictingQueue;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;

/**
 * Keep track of all the incoming requests, we are only keeping track of request originating from
 * WebGoat.
 *
 * @author nbaars
 * @since 8/13/17.
 */
public class WebWolfTraceRepository implements HttpExchangeRepository {
  private enum MatchingMode {
    CONTAINS,
    ENDS_WITH,
    EQUALS;
  }

  record Exclusion(String path, MatchingMode mode) {
    public boolean matches(String path) {
      return switch (mode) {
        case CONTAINS -> path.contains(this.path);
        case ENDS_WITH -> path.endsWith(this.path);
        case EQUALS -> path.equals(this.path);
      };
    }

    public static Exclusion contains(String exclusionPattern) {
      return new Exclusion(exclusionPattern, MatchingMode.CONTAINS);
    }

    public static Exclusion endsWith(String exclusionPattern) {
      return new Exclusion(exclusionPattern, MatchingMode.ENDS_WITH);
    }
  }

  private final EvictingQueue<HttpExchange> traces = EvictingQueue.create(10000);
  private final List<Exclusion> exclusionList =
      List.of(
          contains("/tmpdir"),
          contains("/home"),
          endsWith("/files"),
          contains("/images/"),
          contains("/js/"),
          contains("/webjars/"),
          contains("/requests"),
          contains("/css/"),
          contains("/mail"));

  @Override
  public List<HttpExchange> findAll() {
    return new ArrayList<>(traces);
  }

  private boolean isInExclusionList(String path) {
    return exclusionList.stream().anyMatch(e -> e.matches(path));
  }

  @Override
  public void add(HttpExchange httpTrace) {
    var path = httpTrace.getRequest().getUri().getPath();
    if (!isInExclusionList(path)) {
      traces.add(httpTrace);
    }
  }
}
