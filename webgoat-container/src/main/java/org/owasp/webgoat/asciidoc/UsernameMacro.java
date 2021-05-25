package org.owasp.webgoat.asciidoc;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class UsernameMacro extends InlineMacroProcessor {

    public UsernameMacro(String macroName) {
        super(macroName);
    }

    public UsernameMacro(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    public Object process(ContentNode contentNode, String target, Map<String, Object> attributes) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var username = "unknown";
        if (auth.getPrincipal() instanceof WebGoatUser) {
            username = ((WebGoatUser) auth.getPrincipal()).getUsername();
        }

        //see https://discuss.asciidoctor.org/How-to-create-inline-macro-producing-HTML-In-AsciidoctorJ-td8313.html for why quoted is used
        return createPhraseNode(contentNode, "quoted", username);
    }
}
