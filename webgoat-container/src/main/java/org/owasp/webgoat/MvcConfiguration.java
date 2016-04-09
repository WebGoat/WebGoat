package org.owasp.webgoat;

import org.owasp.webgoat.session.LabelDebugger;
import org.owasp.webgoat.session.WebgoatContext;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;

/**
 *
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/lesson_content").setViewName("lesson_content");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(HammerHead hammerHead) {
        return new ServletRegistrationBean(hammerHead, "/attack/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Resource resource = new ClassPathResource("/plugin_lessons/plugin_lessons_marker.txt");
        try {
            File pluginsDir = resource.getFile().getParentFile();
            registry.addResourceHandler("/plugin_lessons/**").addResourceLocations("file:///" + pluginsDir.toString() + "/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Bean
    public HammerHead hammerHead(WebgoatContext context) {
        return new HammerHead(context);
    }

    @Bean
    //@Scope(value= WebApplicationContext.SCOPE_SESSION)
    public LabelDebugger labelDebugger() {
        return new LabelDebugger();
    }
}