package org.owasp.webgoat.users;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @AllArgsConstructor
    @Getter
    private class Ranking {
        private String username;
        private int flagsCaptured;
    }

    @GetMapping("/scoreboard")
    public List<Ranking> getRankings() {
        List<WebGoatUser> allUsers = userRepository.findAll();
        List<Ranking> rankings = Lists.newArrayList();
        for (WebGoatUser user : allUsers) {
            UserTracker userTracker = userTrackerRepository.findOne(user.getUsername());
            int challengesSolved = challengesSolved(userTracker);
            rankings.add(new Ranking(user.getUsername(), challengesSolved));
        }
        return rankings;
    }

    private int challengesSolved(UserTracker userTracker) {
        List<String> challenges = Lists.newArrayList("Challenge1", "Challenge2", "Challenge3", "Challenge4", "Challenge5");
        List<LessonTracker> challengeTrackers = challenges.stream()
                .map(c -> userTracker.getLessonTracker(c))
                .filter(l -> l.isPresent()).map(l -> l.get()).collect(Collectors.toList());
        return challengeTrackers.size();
    }
}
