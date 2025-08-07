package org.owasp.webgoat.container.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.lessons.LessonResourceScanner;

@Configuration
@RequiredArgsConstructor
public class ResourceConfiguration implements WebMvcConfigurer {

    private final LessonResourceScanner lessonScanner;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // WebGoat internal resources
        addInternalResources(registry);
        
        // WebGoat lesson resources
        addLessonResources(registry);
    }
    
    private void addInternalResources(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
               .addResourceLocations("classpath:/webgoat/static/css/");
        registry.addResourceHandler("/js/**")
               .addResourceLocations("classpath:/webgoat/static/js/");
        registry.addResourceHandler("/plugins/**")
               .addResourceLocations("classpath:/webgoat/static/plugins/");
        registry.addResourceHandler("/fonts/**")
               .addResourceLocations("classpath:/webgoat/static/fonts/");
    }
    
    private void addLessonResources(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
               .addResourceLocations(lessonScanner.applyPattern("classpath:/lessons/%s/images/").toArray(String[]::new));
        registry.addResourceHandler("/lesson_js/**")
               .addResourceLocations(lessonScanner.applyPattern("classpath:/lessons/%s/js/").toArray(String[]::new));
        registry.addResourceHandler("/lesson_css/**")
               .addResourceLocations(lessonScanner.applyPattern("classpath:/lessons/%s/css/").toArray(String[]::new));
        registry.addResourceHandler("/lesson_templates/**")
               .addResourceLocations(lessonScanner.applyPattern("classpath:/lessons/%s/templates/").toArray(String[]::new));
        registry.addResourceHandler("/video/**")
               .addResourceLocations(lessonScanner.applyPattern("classpath:/lessons/%s/video/").toArray(String[]::new));
    }
}