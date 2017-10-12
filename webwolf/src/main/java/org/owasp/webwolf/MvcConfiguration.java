package org.owasp.webwolf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author nbaars
 * @since 8/13/17.
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Value("${webwolf.fileserver.location}")
    private String fileLocatation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + fileLocatation + "/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/WebWolf/home").setViewName("home");
    }

    @PostConstruct
    public void createDirectory() {
        File file = new File(fileLocatation);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}