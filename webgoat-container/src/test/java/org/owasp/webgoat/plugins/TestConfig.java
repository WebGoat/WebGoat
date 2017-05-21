package org.owasp.webgoat.plugins;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Using Fongo for embedded in memory MongoDB testing
 */
@Configuration
public class TestConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongo() throws Exception {
        return new Fongo(getDatabaseName()).getMongo();
    }
}