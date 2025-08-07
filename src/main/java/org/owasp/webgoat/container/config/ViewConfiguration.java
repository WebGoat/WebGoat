package org.owasp.webgoat.container.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import java.nio.charset.StandardCharsets;

@Configuration
public class ViewConfiguration implements WebMvcConfigurer {
    
    private static final String UTF8 = StandardCharsets.UTF_8.displayName();
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/lesson_content").setViewName("lesson_content");
        registry.addViewController("/start.mvc").setViewName("main_new");
    }

    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine thymeleafTemplateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(thymeleafTemplateEngine);
        resolver.setCharacterEncoding(UTF8);
        return resolver;
    }
}
