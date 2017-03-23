package org.owasp.webgoat.users;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.session.LessonTracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Temp endpoint just for the CTF.
 *
 * @author nbaars
 * @since 3/23/17.
 */
@RestController
public class Scoreboard {

    @AllArgsConstructor
    @Getter
    private class Challenge {
        private List<Ranking> rankings;
    }

    @AllArgsConstructor
    @Getter
    private class Ranking {
        private String username;
        private int flagsCaptured;
    }

    private final String webgoatDirectory;

    public Scoreboard(@Value("${webgoat.server.directory}") final String webgoatDirectory) {
        this.webgoatDirectory = webgoatDirectory;
    }

    @GetMapping("/scoreboard")
    public Challenge getRankings() {
        File homeDir = new File(webgoatDirectory);
        File[] files = homeDir.listFiles(f -> f.getName().endsWith(".progress"));
        for (File progressFile : files) {
            String username = progressFile.getName().replace(".progress", "");
            Map<String, LessonTracker> storage = load(progressFile);
            LessonTracker lessonTracker = storage.get("WebGoat Challenge");
            Map<Assignment, Boolean> lessonOverview = lessonTracker.getLessonOverview();
            for (int i = 0; i <= 5; i++) {
                //lessonOverview.e

            }
        }
        return null;
    }

    @SneakyThrows
    private Map<String, LessonTracker> load(File progressFile) {
        Map<String, LessonTracker> storage = Maps.newHashMap();
        if (progressFile.exists() && progressFile.isFile()) {
            DefaultDeserializer deserializer = new DefaultDeserializer(Thread.currentThread().getContextClassLoader());
            try (FileInputStream fis = new FileInputStream(progressFile)) {
                byte[] b = ByteStreams.toByteArray(fis);
                storage = (Map<String, LessonTracker>) deserializer.deserialize(new ByteArrayInputStream(b));
            }
        }
        return storage;
    }

}
