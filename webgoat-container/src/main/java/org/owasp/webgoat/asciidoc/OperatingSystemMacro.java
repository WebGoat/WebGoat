package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;

import java.util.Map;

public class OperatingSystemMacro extends InlineMacroProcessor {

    public OperatingSystemMacro(String macroName) {
        super(macroName);
    }

    public OperatingSystemMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    public Object process(ContentNode contentNode, String target, Map<String, Object> attributes) {
        return System.getProperty("os.name");
    }
}
