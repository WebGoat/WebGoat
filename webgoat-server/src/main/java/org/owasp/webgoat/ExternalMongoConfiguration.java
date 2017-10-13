package org.owasp.webgoat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.io.IOException;

/**
 * If we run
 */
@Configuration
@ConditionalOnProperty(value = "webgoat.embedded.mongo", havingValue = "false")
public class ExternalMongoConfiguration {

    @Autowired
    private MongoProperties properties;

    @Autowired(required = false)
    private MongoClientOptions options;

    @Bean
    public MongodExecutable mongodExecutable() throws IOException {
        return null;
    }

    @Bean
    public MongoDbFactory mongoDbFactory(Environment env) throws Exception {
        MongoClient client = properties.createMongoClient(this.options, env);
        return new SimpleMongoDbFactory(client, properties.getDatabase());
    }
}
