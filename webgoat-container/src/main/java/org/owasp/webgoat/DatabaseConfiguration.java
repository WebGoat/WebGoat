package org.owasp.webgoat;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.owasp.webgoat.service.RestartLessonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class DatabaseConfiguration {

    private String driverClassName;

    public DatabaseConfiguration(@Value("${spring.datasource.driver-class-name}") String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * Define 2 Flyway instances, 1 for WebGoat itself which it uses for internal storage like users and 1 for lesson
     * specific tables we use. This way we clean the data in the lesson database quite easily see {@link RestartLessonService#restartLesson()}
     * for how we clean the lesson related tables.
     */

    @Bean(initMethod = "migrate")
    public Flyway flyWayContainer(DataSource dataSource) {
        return Flyway
                .configure()
                .configuration(Map.of("driver", driverClassName))
                .dataSource(dataSource)
                .schemas("container")
                .locations("db/container")
                .load();
    }

    @Bean
    public Function<String, Flyway> flywayLessons(LessonDataSource lessonDataSource) {
        return schema -> Flyway
                .configure()
                .configuration(Map.of("driver", driverClassName))
                .schemas(schema)
                .dataSource(lessonDataSource)
                .load();
    }

    @Bean
    public LessonDataSource lessonDataSource(DataSource dataSource) {
        return new LessonDataSource(dataSource);
    }
}