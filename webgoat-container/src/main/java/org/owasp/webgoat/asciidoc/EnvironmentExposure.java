package org.owasp.webgoat.asciidoc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Make environment available in the asciidoc code (which you cannot inject because it is handled by the framework)
 */
@Component
public class EnvironmentExposure implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Environment getEnv() {
        return context.getEnvironment();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
