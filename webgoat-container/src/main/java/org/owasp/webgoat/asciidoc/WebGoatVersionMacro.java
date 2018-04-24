package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebGoatVersionMacro extends InlineMacroProcessor {

    public WebGoatVersionMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    protected String process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        return EnvironmentExposure.getEnv().getProperty("webgoat.build.version");
    }
}
