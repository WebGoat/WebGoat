package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Usage in asciidoc:
 * <p>
 * webWolfLink:here[] will display a href with here as text
 * webWolfLink:landing[noLink] will display the complete url, for example: http://WW_HOST:WW_PORT/landing
 */
public class WebWolfMacro extends InlineMacroProcessor {

    public WebWolfMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
	public String process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        Environment env = EnvironmentExposure.getEnv();
        String hostname = determineHost(env.getProperty("webwolf.host"), env.getProperty("webwolf.port"));

        if (displayCompleteLinkNoFormatting(attributes)) {
            return hostname + (hostname.endsWith("/") ? "" : "/") + target;
        }
        return "<a href=\"" + hostname + "\" target=\"_blank\">" + target + "</a>";
    }

    private boolean displayCompleteLinkNoFormatting(Map<String, Object> attributes) {
        return attributes.values().stream().filter(a -> a.equals("noLink")).findFirst().isPresent();
    }

    /**
     * Determine the host from the hostname and ports that were used. 
     * The purpose is to make it possible to use the application behind a reverse proxy. For instance in the docker
     * compose/stack version with webgoat webwolf and nginx proxy. 
     * You do not have to use the indicated hostname, but if you do, you should define two hosts aliases
     * 127.0.0.1 www.webgoat.local www.webwolf.locaal
     */
    private String determineHost(String host, String port) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        host = request.getHeader("Host");
        int semicolonIndex = host.indexOf(":");
        if (semicolonIndex==-1 || host.endsWith(":80")) {
        	host = host.replace(":80", "").replace("www.webgoat.local", "www.webwolf.local");
        } else {
        	host = host.substring(0, semicolonIndex);
        	host = host.concat(":").concat(port);
        }
        return "http://" + host + (includeWebWolfContext() ? "/WebWolf" : "");
    }

    protected boolean includeWebWolfContext() {
        return true;
    }
}
