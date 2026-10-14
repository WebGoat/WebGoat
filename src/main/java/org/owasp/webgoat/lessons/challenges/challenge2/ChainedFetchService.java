/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ChainedFetchService {

  static final String FEED_PATH = "/feeds";
  static final String REDIRECT_PATH = "/go";
  static final String INTERNAL_PATH = "/internal/diagnostics";
  private static final String IMPORT_PATH = "/import";
  private static final int MAX_REDIRECTS = 4;
  private static final int MAX_URL_LENGTH = 4096;

  private final InternalDiagnostic diagnostic;
  private final NorthstarEndpoints endpoints;

  public ChainedFetchService(InternalDiagnostic diagnostic, NorthstarEndpoints endpoints) {
    this.diagnostic = diagnostic;
    this.endpoints = endpoints;
  }

  FetchResult fetch(String url, Challenge2State state, URI importUri) {
    URI current = parse(url);
    String lessonRoot = importUri.getPath().substring(0, importUri.getPath().length() - IMPORT_PATH.length());
    requireApprovedSource(current, importUri, lessonRoot);

    for (int redirects = 0; redirects <= MAX_REDIRECTS; redirects++) {
      if (hasDifferentOrigin(current, importUri)) {
        throw new IllegalArgumentException("This lesson cannot fetch that resource");
      }
      String path = current.getPath();
      if (path.equals(lessonRoot + FEED_PATH)) {
        return new FetchResult(endpoints.feeds(), redirects, false);
      }
      if (path.equals(lessonRoot + REDIRECT_PATH)) {
        if (redirects == MAX_REDIRECTS) {
          throw new IllegalArgumentException("Too many redirects");
        }
        // The source path is checked once. Redirect targets intentionally bypass that check.
        current = followRedirect(current);
      } else if (path.equals(lessonRoot + INTERNAL_PATH)) {
        ResponseEntity<Map<String, String>> response = diagnostic.diagnostics(state.internalToken());
        return new FetchResult(response.getBody().get("flag"), redirects, true);
      } else {
        throw new IllegalArgumentException("This lesson cannot fetch that resource");
      }
    }
    throw new IllegalArgumentException("Too many redirects");
  }

  private void requireApprovedSource(URI source, URI importUri, String lessonRoot) {
    String path = source.getPath();
    if (hasDifferentOrigin(source, importUri)
        || !(path.equals(lessonRoot + FEED_PATH) || path.equals(lessonRoot + REDIRECT_PATH))) {
      throw new IllegalArgumentException("The starting URL is not an approved feed source");
    }
  }

  private URI followRedirect(URI current) {
    String destination =
        UriComponentsBuilder.fromUri(current).build().getQueryParams().getFirst("url");
    if (destination == null) {
      throw new IllegalArgumentException("The redirect needs a destination");
    }
    ResponseEntity<Void> response =
        endpoints.redirect(URLDecoder.decode(destination, StandardCharsets.UTF_8));
    if (!response.getStatusCode().is3xxRedirection()
        || response.getHeaders().getLocation() == null) {
      throw new IllegalArgumentException("The redirect has no valid destination");
    }
    return parse(current.resolve(response.getHeaders().getLocation()).toString());
  }

  private boolean hasDifferentOrigin(URI candidate, URI request) {
    return !candidate.getScheme().equalsIgnoreCase(request.getScheme())
        || !candidate.getHost().equalsIgnoreCase(request.getHost())
        || candidate.getPort() != request.getPort();
  }

  private URI parse(String url) {
    try {
      if (url == null || url.length() > MAX_URL_LENGTH) {
        throw new IllegalArgumentException("Invalid lesson URL");
      }
      URI uri = URI.create(url);
      if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
          || uri.getHost() == null
          || uri.getUserInfo() != null
          || uri.getFragment() != null) {
        throw new IllegalArgumentException("Invalid lesson URL");
      }
      return uri;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid lesson URL", e);
    }
  }

  record FetchResult(String body, int redirects, boolean reachedInternal) {}
}
