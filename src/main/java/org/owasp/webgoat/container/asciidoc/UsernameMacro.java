/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.asciidoc;

import java.util.Map;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsernameMacro extends InlineMacroProcessor {

  public UsernameMacro(String macroName) {
    super(macroName);
  }

  public UsernameMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  public PhraseNode process(
      StructuralNode contentNode, String target, Map<String, Object> attributes) {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    var username = "unknown";
    if (auth.getPrincipal() instanceof WebGoatUser webGoatUser) {
      username = webGoatUser.getUsername();
    }

    // see
    // https://discuss.asciidoctor.org/How-to-create-inline-macro-producing-HTML-In-AsciidoctorJ-td8313.html for why quoted is used
    return createPhraseNode(contentNode, "quoted", username);
  }
}
