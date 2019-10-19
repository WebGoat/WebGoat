package org.owasp.webgoat;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class TestApplication {

    /**
     * We define our own datasource, otherwise we end up with Hikari one which for some lessons will
     * throw an error (feature not supported)
     */
    @Bean
    @ConditionalOnProperty(prefix = "webgoat.start", name = "hsqldb", havingValue = "false")
    public DataSource dataSource(@Value("${spring.datasource.url}") String url) throws SQLException {
        DriverManager.registerDriver(new JDBCDriver());
        return new DriverManagerDataSource(url);
    }
}
