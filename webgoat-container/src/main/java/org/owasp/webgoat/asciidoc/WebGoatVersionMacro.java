package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;

import java.util.Map;

public class WebGoatVersionMacro extends InlineMacroProcessor {

    public WebGoatVersionMacro(String macroName) {
        super(macroName);
    }

    public WebGoatVersionMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
	public Object process(ContentNode contentNode, String target, Map<String, Object> attributes) {
        var webgoatVersion = EnvironmentExposure.getEnv().getProperty("webgoat.build.version");

        //see https://discuss.asciidoctor.org/How-to-create-inline-macro-producing-HTML-In-AsciidoctorJ-td8313.html for why quoted is used
        return createPhraseNode(contentNode, "quoted", webgoatVersion);
    }
}
