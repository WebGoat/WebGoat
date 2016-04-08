package org.owasp.webgoat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class WebGoat extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebGoat.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebGoat.class, args);
    }

//    @Bean
//    @Autowired
//    public TomcatEmbeddedServletContainerFactory servletContainer(final JarScanner jarScanner) {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//        factory.setPort(80);
//        factory.setSessionTimeout(10, TimeUnit.MINUTES);
//        factory.addContextCustomizers(new TomcatContextCustomizer() {
//            @Override
//            public void customize(Context context) {
//
//                context.setJarScanner(jarScanner);
//            }
//        });
//        return factory;
//    }
//
//    @Bean
//    public JarScanner getJarScanner() {
//        StandardJarScanner jarScanner = new StandardJarScanner();
//        jarScanner.setScanClassPath(true);
//        return jarScanner;
//    }


}
