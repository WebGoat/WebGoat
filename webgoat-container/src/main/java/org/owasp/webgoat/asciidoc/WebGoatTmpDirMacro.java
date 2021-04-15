package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;

import java.util.Map;

public class WebGoatTmpDirMacro extends InlineMacroProcessor {

    public WebGoatTmpDirMacro(String macroName) {
        super(macroName);
    }

    public WebGoatTmpDirMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
	public String process(ContentNode contentNode, String target, Map<String, Object> attributes) {
        return EnvironmentExposure.getEnv().getProperty("webgoat.server.directory");
    }
}
