package org.owasp.webgoat.webwolf;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "org.owasp.webgoat.webwolf")
@PropertySource("classpath:application-webwolf.properties")
public class WebWolfApplication {

}
