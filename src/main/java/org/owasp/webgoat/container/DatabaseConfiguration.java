package org.owasp.webgoat.container;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.owasp.webgoat.container.lessons.LessonScanner;
import org.owasp.webgoat.container.service.RestartLessonService;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfiguration {

    private final DataSourceProperties properties;
    private final LessonScanner lessonScanner;

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }

    /**
     * Define 2 Flyway instances, 1 for WebGoat itself which it uses for internal storage like users and 1 for lesson
     * specific tables we use. This way we clean the data in the lesson database quite easily see {@link RestartLessonService#restartLesson()}
     * for how we clean the lesson related tables.
     */
    @Bean(initMethod = "migrate")
    public Flyway flyWayContainer() {
        return Flyway
                .configure()
                .configuration(Map.of("driver", properties.getDriverClassName()))
                .dataSource(dataSource())
                .schemas("container")
                .locations("db/container")
                .load();
    }

    @Bean
    public Function<String, Flyway> flywayLessons(LessonDataSource lessonDataSource) {
        return schema -> Flyway
                .configure()
                .configuration(Map.of("driver", properties.getDriverClassName()))
                .schemas(schema)
                .dataSource(lessonDataSource)
                .locations("lessons")
                .load();
    }

    @Bean
    public LessonDataSource lessonDataSource() {
        return new LessonDataSource(dataSource());
    }
}
