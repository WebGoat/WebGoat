package org.owasp.webgoat.asciidoc;

import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;

public class OperatingSystemMacro extends InlineMacroProcessor {

    public OperatingSystemMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
	public String process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        return System.getProperty("os.name");
    }
}
