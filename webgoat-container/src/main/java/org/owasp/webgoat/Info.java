package org.owasp.webgoat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Info {

    public static class Information {


    }

    @Bean(name = "information")
    public Information information() {
        return null;
    }

}
