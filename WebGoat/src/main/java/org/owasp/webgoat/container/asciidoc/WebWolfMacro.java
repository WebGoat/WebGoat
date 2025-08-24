/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.asciidoc;

import java.util.HashMap;
import java.util.Map;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.InlineMacroProcessor;

/**
 * Usage in asciidoc:
 *
 * <p>webWolfLink:here[] will display a href with here as text
 */
public class WebWolfMacro extends InlineMacroProcessor {

  public WebWolfMacro(String macroName) {
    super(macroName);
  }

  public WebWolfMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  public PhraseNode process(
      StructuralNode contentNode, String linkText, Map<String, Object> attributes) {
    var env = EnvironmentExposure.getEnv();
    var hostname = env.getProperty("webwolf.url");
    var target = (String) attributes.getOrDefault("target", "home");
    var href = hostname + "/" + target;

    // are we using noLink in webWolfLink:landing[noLink]? Then display link with full href
    if (displayCompleteLinkNoFormatting(attributes)) {
      linkText = href;
    }

    var options = new HashMap<String, Object>();
    options.put("type", ":link");
    options.put("target", href);
    attributes.put("window", "_blank");
    return createPhraseNode(contentNode, "anchor", linkText, attributes, options);
  }

  private boolean displayCompleteLinkNoFormatting(Map<String, Object> attributes) {
    return attributes.values().stream().anyMatch(a -> a.equals("noLink"));
  }
}
