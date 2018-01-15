package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebWolfMacro extends InlineMacroProcessor {

    public WebWolfMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    protected String process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        Environment env = EnvironmentExposure.getEnv();
        String hostname = determineHost(env.getProperty("webwolf.host"), env.getProperty("webwolf.port"));
        return "<a href=\"" + hostname + "\" target=\"_blank\">" + target + "</a>";
    }

    /**
     * Look at the remote address from received from the browser first. This way it will also work if you run
     * the browser in a Docker container and WebGoat on your local machine.
     */
    private String determineHost(String host, String port) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String hostname = StringUtils.hasText(ip) ? ip : host;
        return "http://" + hostname + ":" + port + "/WebWolf";
    }
}
