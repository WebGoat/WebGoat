package org.owasp.webgoat.container.asciidoc;

import java.util.Map;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.InlineMacroProcessor;

public class WebGoatVersionMacro extends InlineMacroProcessor {

  public WebGoatVersionMacro(String macroName) {
    super(macroName);
  }

  public WebGoatVersionMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  public PhraseNode process(
      StructuralNode contentNode, String target, Map<String, Object> attributes) {
    var webgoatVersion = EnvironmentExposure.getEnv().getProperty("webgoat.build.version");

    // see
    // https://discuss.asciidoctor.org/How-to-create-inline-macro-producing-HTML-In-AsciidoctorJ-td8313.html for why quoted is used
    return createPhraseNode(contentNode, "quoted", webgoatVersion);
  }
}
