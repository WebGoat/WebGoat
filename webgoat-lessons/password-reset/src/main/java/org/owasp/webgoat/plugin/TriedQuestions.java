package org.owasp.webgoat.plugin;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Set;

@Component
@SessionScope
public class TriedQuestions {

    private Set<String> answeredQuestions = Sets.newHashSet();

    public void incr(String question) {
        answeredQuestions.add(question);
    }

    public boolean isComplete() {
        return answeredQuestions.size() > 1;
    }
}
