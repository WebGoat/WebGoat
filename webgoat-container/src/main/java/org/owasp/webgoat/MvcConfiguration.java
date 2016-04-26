package org.owasp.webgoat;

import org.owasp.webgoat.session.LabelDebugger;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 *
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("pluginTargetDirectory")
    private File pluginTargetDirectory;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/lesson_content").setViewName("lesson_content");
        registry.addViewController("/start.mvc").setViewName("main_new");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(HammerHead hammerHead) {
        return new ServletRegistrationBean(hammerHead, "/attack/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/plugin_lessons/**").addResourceLocations("file:///" + pluginTargetDirectory.toString() + "/");
    }

    @Bean
    public HammerHead hammerHead(WebSession webSession) {
        return new HammerHead(webSession);
    }

    @Bean
    public LabelDebugger labelDebugger() {
        return new LabelDebugger();
    }
}