/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.i18n.Messages;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LabelService {

  public static final String URL_LABELS_MVC = "/service/labels.mvc";
  private final Messages messages;
  private final PluginMessages pluginMessages;

  /**
   * @return a map of all the labels
   */
  @GetMapping(path = URL_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Properties> fetchLabels() {
    var allProperties = new Properties();
    allProperties.putAll(messages.getMessages());
    allProperties.putAll(pluginMessages.getMessages());
    return new ResponseEntity<>(allProperties, HttpStatus.OK);
  }
}
