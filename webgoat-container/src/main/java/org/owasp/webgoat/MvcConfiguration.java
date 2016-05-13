package org.owasp.webgoat;

import com.google.common.collect.Sets;
import org.owasp.webgoat.session.LabelDebugger;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

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
    public TemplateResolver springThymeleafTemplateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setOrder(1);
        resolver.setApplicationContext(applicationContext);
        return resolver;
    }

    @Bean
    public LessonTemplateResolver lessonTemplateResolver() {
        LessonTemplateResolver resolver = new LessonTemplateResolver(pluginTargetDirectory);
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(TemplateResolver springThymeleafTemplateResolver, LessonTemplateResolver lessonTemplateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new SpringSecurityDialect());
        engine.setTemplateResolvers(
                Sets.newHashSet(springThymeleafTemplateResolver, lessonTemplateResolver));
        return engine;
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