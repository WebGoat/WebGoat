package org.owasp.webgoat.asciidoc;

import java.util.Map;

/**
 * Usage in asciidoc:
 * <p>
 * webWolfLink:here[] will display a href with here as text
 * webWolfLink:landing[noLink] will display the complete url, for example: http://WW_HOST:WW_PORT/landing
 */
public class WebWolfRootMacro extends WebWolfMacro {

    public WebWolfRootMacro(String macroName) {
        super(macroName);
    }

    public WebWolfRootMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    protected boolean includeWebWolfContext() {
        return false;
    }
}
