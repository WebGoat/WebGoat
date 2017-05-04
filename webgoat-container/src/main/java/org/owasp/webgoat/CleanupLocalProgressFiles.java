package org.owasp.webgoat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author nbaars
 * @since 4/15/17.
 */
@Slf4j
@Configuration
@ConditionalOnExpression("'${webgoat.clean}' == 'true'")
public class CleanupLocalProgressFiles {

    @Value("${webgoat.server.directory}")
    private String webgoatHome;

    @PostConstruct
    public void clean() {
        File dir = new File(webgoatHome);
        if (dir.exists()) {
            File[] progressFiles = dir.listFiles(f -> f.getName().endsWith(".progress"));
            if (progressFiles != null) {
                log.info("Removing stored user preferences...");
                for (File f : progressFiles) {
                    f.delete();
                }
            }
        }
    }
}
