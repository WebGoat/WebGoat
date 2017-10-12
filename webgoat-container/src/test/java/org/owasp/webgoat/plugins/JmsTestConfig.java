package org.owasp.webgoat.plugins;

import org.apache.activemq.broker.BrokerService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nbaars
 * @since 8/30/17.
 */
@Configuration
public class JmsTestConfig {

    @Bean
    public BrokerService broker() throws Exception {
        return Mockito.mock(BrokerService.class);
    }
}
