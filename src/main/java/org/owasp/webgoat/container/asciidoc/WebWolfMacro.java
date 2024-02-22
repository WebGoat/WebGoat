package org.owasp.webgoat.container.asciidoc;

import java.util.HashMap;
import java.util.Map;
import org.asciidoctor.ast.ContentNode;
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
  public Object process(ContentNode contentNode, String linkText, Map<String, Object> attributes) {
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
    return createPhraseNode(contentNode, "anchor", linkText, attributes, options).convert();
  }

  private boolean displayCompleteLinkNoFormatting(Map<String, Object> attributes) {
    return attributes.values().stream().anyMatch(a -> a.equals("noLink"));
  }
}
