/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.assignments;

import org.owasp.webgoat.container.i18n.PluginMessages;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/** This class intercepts the response body and applies the plugin messages to the attack result. */
@RestControllerAdvice
public class AttackResultMessageResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  private final PluginMessages pluginMessages;

  public AttackResultMessageResponseBodyAdvice(PluginMessages pluginMessages) {
    this.pluginMessages = pluginMessages;
  }

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof AttackResult a) {
      return a.apply(pluginMessages);
    }
    return body;
  }
}
