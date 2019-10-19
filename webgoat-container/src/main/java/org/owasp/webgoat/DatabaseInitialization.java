package org.owasp.webgoat;

import org.flywaydb.core.Flyway;
import org.owasp.webgoat.service.RestartLessonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Define 2 Flyway instances, 1 for WebGoat itself which it uses for internal storage like users and 1 for lesson
 * specific tables we use. This way we clean the data in the lesson database quite easily see {@link RestartLessonService#restartLesson()}
 * for how we clean the lesson related tables.
 */
@Configuration
public class DatabaseInitialization {

    private final DataSource dataSource;

    public DatabaseInitialization(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyWayContainer() {
        return Flyway
                .configure().configuration(
                        Map.of("driver", "org.hsqldb.jdbc.JDBCDriver"))
                .dataSource(dataSource)
                .schemas("container")
                .locations("db/container")
                .load();
    }

    @Bean(initMethod = "migrate")
    @DependsOn("flyWayContainer")
    public Flyway flywayLessons() {
        return Flyway
                .configure().configuration(
                        Map.of("driver", "org.hsqldb.jdbc.JDBCDriver"))
                .dataSource(dataSource)
                .load();
    }
}