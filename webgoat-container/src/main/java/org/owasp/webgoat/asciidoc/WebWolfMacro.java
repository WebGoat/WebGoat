package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Usage in asciidoc:
 * <p>
 * webWolfLink:here[] will display a href with here as text
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
        var hostname = determineHost(env.getProperty("webwolf.host"), env.getProperty("webwolf.port"));
        var target = (String) attributes.getOrDefault("target", "home");
        var href = hostname + "/" + target;

        //are we using noLink in webWolfLink:landing[noLink]? Then display link with full href
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
        return attributes.values().stream().filter(a -> a.equals("noLink")).findFirst().isPresent();
    }

    /**
     * Determine the host from the hostname and ports that were used.
     * The purpose is to make it possible to use the application behind a reverse proxy. For instance in the docker
     * compose/stack version with webgoat webwolf and nginx proxy.
     * You do not have to use the indicated hostname, but if you do, you should define two hosts aliases
     * 127.0.0.1 www.webgoat.local www.webwolf.local
     */
    private String determineHost(String host, String port) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        host = request.getHeader("Host");
        int semicolonIndex = host.indexOf(":");
        if (semicolonIndex == -1 || host.endsWith(":80")) {
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
