package org.owasp.webgoat;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


/**
 * Rationale for this class: when the HSQLDB is started with jdbc:file:// it is only accessible from within the same
 * JVM. This can only be done if you start a standalone HSQLDB. We need both WebWolf and WebGoat to use the same database
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "webgoat.start", name = "hsqldb", havingValue = "true")
public class HSQLDBDatabaseConfig {

    @Value("${hsqldb.port:9001}")
    private int hsqldbPort;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server hsqlStandalone(@Value("${webgoat.server.directory}") String directory,
                                 @Value("${hsqldb.silent:true}") boolean silent,
                                 @Value("${hsqldb.trace:false}") boolean trace) {
        log.info("Starting internal database on port {} ...", hsqldbPort);
        Server server = new Server();
        server.setDatabaseName(0, "webgoat");
        server.setDatabasePath(0, directory + "/data/webgoat");
        server.setDaemon(true);
        server.setTrace(trace);
        server.setSilent(silent);
        server.setPort(hsqldbPort);
        return server;
    }

    @Primary
    @Bean
    @DependsOn("hsqlStandalone")
    public DataSource dataSource(@Value("${spring.datasource.driver-class-name}") String driverClass,
                                 @Value("${spring.datasource.url}") String url) {
        return DataSourceBuilder.create()
                .driverClassName(driverClass)
                .url(url)
                .build();
    }
}
