package org.owasp.webgoat.container;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "org.owasp.webgoat.container")
@PropertySource("classpath:application-webgoat.properties")
public class WebGoatApplication {

}
