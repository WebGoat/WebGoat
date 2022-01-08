package org.owasp.webgoat.container.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Temp endpoint just for the CTF.
 *
 * @author nbaars
 * @since 3/23/17.
 */
@RestController
@AllArgsConstructor
public class Scoreboard {

    private final UserTrackerRepository userTrackerRepository;
    private final UserRepository userRepository;
    private final Course course;
    private final PluginMessages pluginMessages;

    @AllArgsConstructor
    @Getter
    private class Ranking {
        private String username;
        private List<String> flagsCaptured;
    }

    @GetMapping("/scoreboard-data")
    public List<Ranking> getRankings() {
        List<WebGoatUser> allUsers = userRepository.findAll();
        List<Ranking> rankings = new ArrayList<>();
        for (WebGoatUser user : allUsers) {
            if (user.getUsername().startsWith("csrf-")) {
                //the csrf- assignment specific users do not need to be in the overview
                continue;
            }
            UserTracker userTracker = userTrackerRepository.findByUser(user.getUsername());
            rankings.add(new Ranking(user.getUsername(), challengesSolved(userTracker)));
        }
        /* sort on number of captured flags to present an ordered ranking */
        rankings.sort((o1, o2) -> o2.getFlagsCaptured().size() - o1.getFlagsCaptured().size());
        return rankings;
    }

    private List<String> challengesSolved(UserTracker userTracker) {
        List<String> challenges = List.of("Challenge1", "Challenge2", "Challenge3", "Challenge4", "Challenge5", "Challenge6", "Challenge7", "Challenge8", "Challenge9");
        return challenges.stream()
                .map(userTracker::getLessonTracker)
                .flatMap(Optional::stream)
                .filter(LessonTracker::isLessonSolved)
                .map(LessonTracker::getLessonName)
                .map(this::toLessonTitle)
                .toList();
    }

    private String toLessonTitle(String id) {
        String titleKey = course.getLessons().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(Lesson::getTitle)
                .orElse("No title");
        return pluginMessages.getMessage(titleKey, titleKey);
    }
}
