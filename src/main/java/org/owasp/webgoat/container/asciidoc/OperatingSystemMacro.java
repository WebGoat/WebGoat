/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.asciidoc;

import java.util.Map;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.InlineMacroProcessor;

public class OperatingSystemMacro extends InlineMacroProcessor {

  public OperatingSystemMacro(String macroName) {
    super(macroName);
  }

  public OperatingSystemMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  public PhraseNode process(
      StructuralNode contentNode, String target, Map<String, Object> attributes) {
    var osName = System.getProperty("os.name");

    // see
    // https://discuss.asciidoctor.org/How-to-create-inline-macro-producing-HTML-In-AsciidoctorJ-td8313.html for why quoted is used
    return createPhraseNode(contentNode, "quoted", osName);
  }
}
