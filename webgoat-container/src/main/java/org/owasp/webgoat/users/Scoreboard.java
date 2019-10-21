package org.owasp.webgoat.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.session.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            UserTracker userTracker = userTrackerRepository.findByUser(user.getUsername());
            rankings.add(new Ranking(user.getUsername(), challengesSolved(userTracker)));
        }
        return rankings;
    }

    private List<String> challengesSolved(UserTracker userTracker) {
        List<String> challenges = List.of("Challenge1", "Challenge2", "Challenge3", "Challenge4", "Challenge5", "Challenge6", "Challenge7", "Challenge8", "Challenge9");
        return challenges.stream()
                .map(c -> userTracker.getLessonTracker(c))
                .filter(l -> l.isPresent()).map(l -> l.get())
                .filter(l -> l.isLessonSolved())
                .map(l -> l.getLessonName())
                .map(l -> toLessonTitle(l))
                .collect(Collectors.toList());
    }

    private String toLessonTitle(String id) {
        String titleKey = course.getLessons().stream().filter(l -> l.getId().equals(id)).findFirst().get().getTitle();
        return pluginMessages.getMessage(titleKey, titleKey);
    }
}
